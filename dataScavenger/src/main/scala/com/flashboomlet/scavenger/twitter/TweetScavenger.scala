package com.flashboomlet.scavenger.twitter

import java.text.Normalizer
import java.util.Date

import com.flashboomlet.data.models.MetaData
import com.flashboomlet.preproccessing.FastSentimentClassifier
import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities.StatusSearch
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.entities.enums.ResultType
import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.data.models.Entity
import com.flashboomlet.data.models.PreprocessData
import com.flashboomlet.data.models.FinalTweet
import com.flashboomlet.data.models.TweetSocialData
import com.flashboomlet.data.models.TwitterSearch
import com.flashboomlet.db.MongoDatabaseDriver
import com.flashboomlet.preproccessing.CountUtil.countContent
import com.flashboomlet.preproccessing.DateUtil
import com.flashboomlet.scavenger.Scavenger
import com.flashboomlet.scavenger.twitter.configuration.TwitterConfiguration
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.util.Try

/**
  * Created by ttlynch on 5/31/16.
  *
  * Implementation of the Twitter4s API which can be found as:
  *   https://github.com/DanielaSfregola/twitter4s
  */
class TweetScavenger(implicit val mapper: ObjectMapper,
    implicit val db: MongoDatabaseDriver) extends Scavenger with LazyLogging {

  final val count = 100

  final val comma = ","

  final val one = 1

  final val four = 4

  val twitterConfiguration = TwitterConfiguration()

  val client = new TwitterClient(twitterConfiguration.getConsumerToken,
    twitterConfiguration.getAccessToken)

  private[this] val PolitenessDelay: Int = 1000


  /**
    * Scaffold for the scavengerTrait
    */
  def scavenge(entities: Set[Entity]): Unit = {
    // get highest ID for a given search parameter from entity
    entities.foreach { entity =>
      val searchTerms = entity.searchTerms
      searchTerms.foreach { query =>
        Try {
          val twitterSearch: TwitterSearch = getTweetSearchSinceId(query, entity.lastName)
          // In the future Search using: searchTweetsFrom(query, sinceID)
          Thread.sleep(PolitenessDelay) // TODO: calculate based on query rate
          searchTweetsFrom(
            query = query,
            sinceID = Some(twitterSearch.recentTwitterId),
            entityLastName = entity.lastName)
          .foreach { tweets =>
            tweets.foreach { (tweet: Tweet) =>
              getAndInsertFinalTweet(
                tweet = tweet,
                entityLastName = entity.lastName,
                query = query)
            }
            // This should work but doesn't
            //db.updateTwitterSearch(TwitterSearch(query, entity.lastName, tweets.map(_.id).max))
          }
          logger.info(s"Successfully inserted tweets for ${entity.lastName}, $query")
        }.getOrElse(
          logger.error(s"Failed to fetch and inserts tweets for query: $query")
        )
      }
    }
  }

  /**
    * Get the most recent since id for a specific search for a specific entity
    *
    * @param query Query associated with the search requiring a sinceid
    * @param entityLastName the entity last name associated with the query associated with the
    *                       search requiring a sinceid
    * @return A sinceid that may be used
    */
  def getTweetSearchSinceId(query: String, entityLastName: String): TwitterSearch = {
    db.getTwitterSearch(query, entityLastName) match {
      case Some(ts) => ts
      case None =>
        val recentTweetId: Long = getRecentTweet(query)
        val twitterSearch = TwitterSearch(
          query = query,
          entityLastName = entityLastName,
          recentTwitterId = recentTweetId
        )
        db.insertTwitterSearch(twitterSearch)
        twitterSearch
    }
  }

  /**
    * Creates the metaData object for the tweet
    *
    * @param tweetDate the date of the tweet
    * @param query the query that got the tweet
    * @return a metaData object
    */
  private def getMetaData(
    entityLastName: String,
    tweetDate: Date,
    query: String): MetaData = {

      MetaData(
        fetchDate = DateUtil.getNowInMillis,
        publishDate = DateUtil.getTweetInMillis(tweetDate),
        source = "Twitter",
        searchTerm = query,
        entityLastName = entityLastName,
        contributions = 0
      )
  }

  /**
    * Creates the finalTweet object for the tweet
    *
    * @param tweet the tweet to convert into a final object
    * @param query the query that got the tweet
    * @return a finalTweet object
    */
  private def getAndInsertFinalTweet(
      tweet: Tweet,
      query: String,
      entityLastName: String): Unit = {

    val metaData = getMetaData(
      entityLastName = entityLastName,
      tweetDate = tweet.created_at,
      query = query)

    if (db.tweetExists(tweet.id)) {
      db.addTweetMetaData(tweet.id, metaData)
    } else {
      val cleanText = flattenToAscii(tweet.text)
      val sentiment = FastSentimentClassifier.getSentiment(cleanText)
      val counts = countContent("", cleanText, query)
      val preprocessData = PreprocessData(sentiment, counts)

      val finalTweet = FinalTweet(
        tweetID = tweet.id,
        content = cleanText,
        followersCount = tweet.user.get.followers_count,
        friendsCount = tweet.user.get.friends_count,
        userID = tweet.user.get.id,
        name = tweet.user.get.name,
        screenName = tweet.user.get.screen_name,
        socialDatas = Set(
          TweetSocialData(
            favoriteCount = tweet.favorite_count,
            retweetCount = tweet.retweet_count,
            fetchDate = DateUtil.getNowInMillis
          )
        ),
        country = tweet.place match {
          case Some(g) => g.country
          case None => "N/A"
        },
        parentTweetId = tweet.retweeted_status match {
          case Some(t) => t.id
          case None => 0
        },
        isRetweet = tweet.retweeted,
        metaDatas = Set(metaData),
        preprocessData = preprocessData
      )
      db.insertTweet(finalTweet)
    }
  }

  /**
    * Normalizes a string to ascii while preserving the non ascii characters as their closest
    * normalized latin relative.
    *
    * Algorithm is from :
    * http://stackoverflow.com/
    * questions/3322152/
    * is-there-a-way-to-get-rid-of-accents-and-convert-a-whole-string-to-regular-lette/
    * 15191508#15191508
    *
    * @param dirty string to clean up
    * @return cleaned string
    */
  private[this] def flattenToAscii(dirty: String): String = {
    val out: Array[Char] = new Array(dirty.length())
    val normalizedString = Normalizer.normalize(dirty, Normalizer.Form.NFD)
    (0 until normalizedString.length).filter { n =>
      normalizedString.charAt(n) <= '\u007F'
    }.zipWithIndex.foreach((t: (Int, Int)) =>
      out(t._2) = normalizedString.charAt(t._1)
    )
    val clean = new String(out)
    clean.replaceFirst("^RT @[^:]+:\\s+", "")
  }


  /**
    * searchForNTweets is a general function to search tweets based on specific needs.
    *
    * The basic idea behind this function is to make a search, and then output the search if the
    *   result count of the returned query is less than 100, indicating that you have reached
    *   the latest tweet of the day.
    *
    * @param query has a 500 character limit.
    * @param sinceID you can get all tweets with an ID greater than X value.
    * @return A list of cleaned tweets from the given Query
    */
  private def searchTweetsFrom(
    query: String,
    entityLastName: String,
    sinceID: Option[Long] = None): Future[Seq[Tweet]] = {

    client.searchTweet(
      query = query,
      count = count,
      include_entities = false,
      result_type = ResultType.Recent,
      language = Some(Language.English),
      since_id = sinceID
    ).map { tweets =>
      tweets.statuses.filter((tweet: Tweet) =>
        !db.isTweetDuplicate(query, entityLastName, tweet.id)
      )
    }
  }

  /**
    * Get Recent Tweet Returns a single Tweet in the final Tweet Format
 *
    * @param query the query that you are searching on
    * @return a single tweet in the Final Tweet Format
    */
  private def getRecentTweet(query: String): Long = {
    val future = client.searchTweet(
      query = query,
      count = 1,
      include_entities = false,
      result_type = ResultType.Recent,
      language = Some(Language.English)
    ).map { (statusSearch: StatusSearch) =>
      statusSearch.statuses.head.id
    }
    Await.result(future, Duration.Inf)
  }
}

/** Companion object with a constructor that retrieves configurations */
object TweetScavenger {
  def apply()(implicit mapper: ObjectMapper, db: MongoDatabaseDriver): TweetScavenger = {
    new TweetScavenger()
  }
}
