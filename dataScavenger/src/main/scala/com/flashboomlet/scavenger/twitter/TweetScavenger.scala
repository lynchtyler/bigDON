package com.flashboomlet.scavenger.twitter

import java.text.SimpleDateFormat
import java.util.Date

import com.flashboomlet.data.models.MetaData
import com.flashboomlet.preproccessing.FastSentimentClassifier
import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities.StatusSearch
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.entities.enums.ResultType
import com.danielasfregola.twitter4s.http.unmarshalling.CustomSerializers
import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.data.models.Entity
import com.flashboomlet.data.models.PreprocessData
import com.flashboomlet.data.models.FinalTweet
import com.flashboomlet.data.models.TwitterSearch
import com.flashboomlet.db.MongoDatabaseDriver
import com.flashboomlet.preproccessing.CountUtil.countContent
import com.flashboomlet.preproccessing.DateUtil.getToday
import com.flashboomlet.scavenger.Scavenger
import com.flashboomlet.scavenger.twitter.configuration.TwitterConfiguration
import com.typesafe.scalalogging.LazyLogging
import org.json4s.DefaultFormats
import org.json4s.Formats

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


  /**
    * Scaffold for the scavengerTrait
    */
  def scavenge(entities: Set[Entity]): Unit = {
    // get highest ID for a given search parameter from entity
    entities.foreach { entity =>
      val searchTerms = entity.searchTerms
      val today = getToday()
      searchTerms.foreach { query =>
        Try {
          val twitterSearch: TwitterSearch = getTweetSearchSinceId(query, entity.lastName)
          // In the future Search using: searchTweetsFrom(query, sinceID)
          searchTweetsFrom(
            query = query,
            sinceID = Some(twitterSearch.recentTwitterId),
            entityLastName = entity.lastName)
          .foreach { tweets =>
            tweets.foreach { (tweet: Tweet) =>
              getAndInsertFinalTweet(tweet, query, today)
            }
          }
          db.updateTwitterSearch(twitterSearch)
          logger.info(s"Successfully inserted tweets for ${entity.lastName}, ${query}")
        }.getOrElse(
          logger.error(s"Failed to fetch and inserts tweets for query: ${query}")
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
    * @param today today's date
    * @param tweetDate the date of the tweet
    * @param query the query that got the tweet
    * @return a metaData object
    */
  private def getMetaData(
    today: String,
    tweetDate: Date,
    query: String): MetaData = {

      MetaData(
        fetchDate = today,
        publishDate = tweetDate.toString,
        source = "Twiiter",
        searchTerm = query,
        entityLastName = "", // TODO
        contributions = 0
      )
  }

  /**
    * Creates the finalTweet object for the tweet
    *
    * @param tweet the tweet to convert into a final object
    * @param query the query that got the tweet
    * @param today today's date
    * @return a finalTweet object
    */
  private def getAndInsertFinalTweet(
      tweet: Tweet,
      query: String,
      today: String): Unit = {
    val metaData = getMetaData(today, tweet.created_at, query)

    if (db.tweetExists(tweet.id)) {
      db.addTweetMetaData(tweet.id, metaData)
    } else {
      val sentiment = FastSentimentClassifier.getSentiment(tweet.text)
      val counts = countContent("", tweet.text, query)
      val preprocessData = PreprocessData(sentiment, counts)


      val finalTweet = FinalTweet(
        tweetID = tweet.id,
        content = tweet.text,
        followersCount = tweet.user.get.followers_count,
        friendsCount = tweet.user.get.friends_count,
        userID = tweet.user.get.id,
        name = tweet.user.get.name,
        screenName = tweet.user.get.screen_name,
        favoriteCount = tweet.favorite_count,
        country = tweet.place match {
            case Some(g) => g.country
            case None => "N/A"
        },
        retweetCount = tweet.retweet_count,
        metaDatas = Set(metaData),
        preprocessData = preprocessData
      )
      db.insertTweet(finalTweet)
    }
  }

  /**
    * getUserTimelineFor is a general function to search the top 100 tweets of a sepcific users
    *   timeline
    *
    * This is a simple request the top 100 tweets and bam!
    *
    * @param ID
    * @param tweetCount
    * @param maxID
    * @return
    */
  private def getUserTimelineFor(
    ID: Long,
    tweetCount: Integer = count,
    maxID: Option[Long] = None): Future[Seq[Tweet]] =  {

    client.getUserTimelineForUserId(
    user_id = ID,
    count = tweetCount,
    max_id = maxID)
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
    val today = getToday()
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

  private implicit def json4sFormats: Formats = defaultFormats ++ CustomSerializers.all

  /**
    * Formatter for dates, specifically used for JSON formatting
    */
  private val defaultFormats = new DefaultFormats {
    override def dateFormatter = {
      val simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy")
      simpleDateFormat
    }
  }

  private[this] def getString(field: String): String = Option(field).getOrElse("")
}

/** Companion object with a constructor that retrieves configurations */
object TweetScavenger {
  def apply()(implicit mapper: ObjectMapper, db: MongoDatabaseDriver): TweetScavenger = {
    new TweetScavenger()
  }
}
