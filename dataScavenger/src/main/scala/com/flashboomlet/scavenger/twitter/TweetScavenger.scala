package com.flashboomlet.scavenger.twitter

import java.text.SimpleDateFormat
import java.util.Date

import com.flashboomlet.data.models.MetaData
import com.flashboomlet.preproccessing.FastSentimentClassifier

import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.entities.enums.ResultType
import com.danielasfregola.twitter4s.http.unmarshalling.CustomSerializers
import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.data.models.Entity
import com.flashboomlet.data.models.PreprocessData
import com.flashboomlet.data.models.FinalTweet
import com.flashboomlet.preproccessing.CountUtil.countContent
import com.flashboomlet.preproccessing.DateUtil.getToday
import com.flashboomlet.preproccessing.DateUtil.convertTwitterDate
import com.flashboomlet.scavenger.Scavenger
import com.flashboomlet.scavenger.twitter.configuration.TwitterConfiguration
import org.json4s.DefaultFormats
import org.json4s.Formats
import org.json4s.native.JsonMethods.parse
import org.json4s.native.JsonMethods.pretty
import org.json4s.native.JsonMethods.render
import org.json4s.native.Serialization

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by ttlynch on 5/31/16.
  *
  * Implementation of the Twitter4s API which can be found as:
  *   https://github.com/DanielaSfregola/twitter4s
  */
class TweetScavenger(implicit val mapper: ObjectMapper) extends Scavenger {

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
  def scavenge(entity: Entity): Unit = {
    // get highest ID for a given search parameter from entity
    val searchTerms = entity.searchTerms
    val today = getToday()
    searchTerms.map { query =>
      // In the future Search using: searchTweetsFrom(query, sinceID)
      searchTweetsFrom(query).map { tweets =>
        tweets.foreach { tweet =>
          val finalTweet = getFinalTweet(tweet, query, today)
          // Insert into DB
        }
      }
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
        publishDate = convertTwitterDate(tweetDate),
        source = "Twiiter",
        searchTerm = query,
        entityId = "", // TODO
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
  private def getFinalTweet(
    tweet: Tweet,
    query: String,
    today: String): FinalTweet = {

    val sentiment = FastSentimentClassifier.getSentiment(tweet.text)
    val counts = countContent("", tweet.text, query)
    val preprocessData = PreprocessData(sentiment, counts)
    val metaData = getMetaData(today, tweet.created_at, query)

    FinalTweet(
      tweetID = tweet.id,
      content = tweet.text,
      followersCount = tweet.user.get.followers_count,
      friendsCount = tweet.user.get.friends_count,
      userID = tweet.user.get.id,
      name = tweet.user.get.name,
      screenName = tweet.user.get.screen_name,
      favoriteCount = tweet.favorite_count,
      country = tweet.place.get.country,
      retweetCount = tweet.retweet_count,
      metaData: MetaData,
      preprocessData: PreprocessData
    )
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
    sinceID: Option[Long] = None): Future[Seq[Tweet]] = {

    client.searchTweet(
      query = query,
      count = count,
      include_entities = false,
      result_type = ResultType.Recent,
      language = Some(Language.English),
      since_id = sinceID
    ).map { tweets =>
      tweets.statuses
    }
  }

  /**
    * Get Recent Tweet Returns a single Tweet in the final Tweet Format
 *
    * @param query the query that you are searching on
    * @return a single tweet in the Final Tweet Format
    */
  private def getRecentTweet(query: String): Future[FinalTweet] = {
    val today = getToday()
    client.searchTweet(
      query = query,
      count = 1,
      include_entities = false,
      result_type = ResultType.Recent,
      language = Some(Language.English)
    ).map { tweets =>
      getFinalTweet(tweets.statuses.head, query, today)
    }
  }

  /**
    * QueriedCount takes a string of metaData and parses out the amount of tweets queried.
    *
    * This is a great helper function to get all of the tweets from X tweet and helps
    * searchTweetsFrom by informing it when to stop gathering new tweets as there are none.
    *
    * @param params metaData
    * @return number of tweets queried
    */
  private def queriedCount(params: String): Long = {
    params.split(comma).takeRight(3).take(one).toString.toLong
  }

  /**
    * extractNextMaxID takes a string of metaData and parses out the next MaxID to search from.
    *
    * This function aids searchForNTweets in collecting N number of tweets by getting the next
    * batch of tweets
    *
    * @param params metaData
    * @return the next max ID
    */
  private def extractNextMaxId(params: String): Option[Long] = {
    params.split("&").find(_.contains("max_id")).map(_.split("=")(one).toLong)
  }

  /**
    * extractNextSinceId takes a string of metaData and parses out the next sinceID to search from.
    *
    * This function aids searchTweetsFrom by informing it of where to get the next set of
    * tweets from
    *
    * @param params metaData
    * @return the next sinceID to query
    */
  private def extractNextSinceId(params: String): Option[Long] = {
    val s = params.split(comma).takeRight(four).take(one)
    s.toString.split("&").find(_.contains("since_id")).map(_.split("=")(1).toLong)
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

  /**
    * toJson converts a type T to a String output of formatted JSON specific to the T
    *
    * @param value raw data
    * @tparam T type of data
    * @return formatted JSON specific to the T
    */
  private def toJson[T <: AnyRef](value: T): String =
    pretty(render(parse(Serialization.write(value))))

}

/** Companion object with a constructor that retrieves configurations */
object TweetScavenger {
  def apply()(implicit mapper: ObjectMapper): TweetScavenger = {
    new TweetScavenger()
  }
}
