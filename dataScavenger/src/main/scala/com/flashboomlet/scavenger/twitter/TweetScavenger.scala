package com.flashboomlet.scavenger.twitter

import java.text.SimpleDateFormat

import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities.StatusSearch
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.entities.enums.ResultType
import com.danielasfregola.twitter4s.entities.enums.ResultType.ResultType
import com.danielasfregola.twitter4s.http.unmarshalling.CustomSerializers
import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.data.models.Entity
import com.flashboomlet.scavenger.Scavenger
import com.flashboomlet.scavenger.twitter.configuration.TwitterConfiguration
import com.flashboomlet.scavenger.twitter.twitterResponses.ShortTweetResponse
import com.flashboomlet.scavenger.twitter.twitterResponses.ShortUserResponse
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
class TweetScavenger extends Scavenger {

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
  def scavenge(entity: Entity): Unit = {}

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
    maxID: Option[Long] = None): Future[Seq[ShortTweetResponse]] =  {

    client.getUserTimelineForUserId(
    user_id = ID,
    count = tweetCount,
    max_id = maxID).map { tweets =>
      toShortTweet(tweets)
    }
  }

  /**
    * searchForNTweets is a general function to search tweets based on specific needs.
    *
    * The basic idea behind this function is to make a search, and then output the search if the
    *   tweetCount of the next potential query is less than or equal to zero, indicating that you
    *   have reached the desired amount of tweets.
    *
    * @param query has a 500 character limit.
    * @param tweetCount has a default value of 100. For more use searchTweetHistory
    * @param results you can either get ResultType.Mix/Popular/Recent. Default of Popular
    * @param maxID you can get all tweets prior to an ID less than X value.
    * @return A list of cleaned tweets from the given Query
    */
  private def searchForNTweets(
    query: String,
    tweetCount: Long = count,
    results: ResultType = ResultType.Popular,
    maxID: Option[Long] = None): Future[Seq[ShortTweetResponse]] = {

    client.searchTweet(
      query = query,
      count = count,
      include_entities = false,
      result_type = results,
      language = Some(Language.English),
      max_id = maxID
    ).map { tweets =>
      val cleanTweets = toShortTweet(tweets)

      val nextMaxId = extractNextMaxId(tweets.search_metadata.next_results)
      if ((count-100) <= 0)
      {
        cleanTweets
      } else {
        val rest = searchForNTweets(
          query: String,
          tweetCount-100,
          results,
          nextMaxId)
        rest.map { search =>
          cleanTweets ++ search
        }
        cleanTweets
      }
    }
  }

  /**
    * searchForNTweets is a general function to search tweets based on specific needs.
    *
    * The basic idea behind this function is to make a search, and then output the search if the
    *   result count of the returned query is less than 100, indicating that you have reached
    *   the latest tweet of the day.
    *
    * @param query has a 500 character limit.
    * @param tweetCount has a default value of 100. For more use searchTweetHistory
    * @param results you can either get ResultType.Mix/Popular/Recent. Default of Popular
    * @param sinceID you can get all tweets with an ID greater than X value.
    * @return A list of cleaned tweets from the given Query
    */
  private def searchTweetsFrom(
    query: String,
    tweetCount: Long = count,
    results: ResultType = ResultType.Popular,
    sinceID: Option[Long] = None): Future[Seq[ShortTweetResponse]] = {

    client.searchTweet(
      query = query,
      count = count,
      include_entities = false,
      result_type = results,
      language = Some(Language.English),
      since_id = sinceID
    ).map { tweets =>

      val resultCount = queriedCount(tweets.search_metadata.toString)
      val nextSinceId = extractNextSinceId(tweets.search_metadata.toString)
      val cleanTweets = toShortTweet(tweets)

      if (resultCount < 100)
      {
        cleanTweets
      } else {
        val rest = searchTweetsFrom(
          query: String,
          count,
          results,
          nextSinceId)
        rest.map { search =>
          cleanTweets ++ search
        }
        cleanTweets
      }
    }
  }

  /**
    * toShortTweet takes a list of tweets from the twitter API and cleans them up.
    *   It takes one list of a type and converts it to another.
    *
    * @param tweets is a statusSearch object
    * @return is a list of shortened tweets
    */
  private def toShortTweet(tweets: StatusSearch): Seq[ShortTweetResponse] = {
    tweets.statuses.map(tweet =>
      ShortTweetResponse(
        id = tweet.id,
        text = tweet.text,
        user = tweet.user.map(user =>
          ShortUserResponse(
            followers_count = user.followers_count,
            friends_count = user.friends_count,
            id = user.id,
            name = user.name,
            screen_name = user.screen_name
          )
        ),
        coordinates = tweet.coordinates,
        created_at = tweet.created_at,
        favorite_count = tweet.favorite_count,
        place = tweet.place,
        retweet_count = tweet.retweet_count
      )
    )
  }

  /**
    * toShortTweet takes a list of tweets from the twitter API and cleans them up.
    *   It takes one list of a type and converts it to another.
    *
    * @param tweets is a seq of tweet objects
    * @return is a list of shortened tweets
    */
  private def toShortTweet(tweets: Seq[Tweet]): Seq[ShortTweetResponse] = {
    tweets.map(tweet =>
      ShortTweetResponse(
        id = tweet.id,
        text = tweet.text,
        user = tweet.user.map(user =>
          ShortUserResponse(
            followers_count = user.followers_count,
            friends_count = user.friends_count,
            id = user.id,
            name = user.name,
            screen_name = user.screen_name
          )
        ),
        coordinates = tweet.coordinates,
        created_at = tweet.created_at,
        favorite_count = tweet.favorite_count,
        place = tweet.place,
        retweet_count = tweet.retweet_count
      )
    ).toList
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

  /**
    *
    * @param query has a 500 character limit.
    * @param tweetCount has a default value of 100. For more use searchTweetHistory
    * @return A list of cleaned tweets from the given Query
    */
  def searchNPopularTweets(
    query: String,
    tweetCount: Int = count): Future[Seq[ShortTweetResponse]] = {
    searchForNTweets(query, tweetCount)
  }

  /**
    *
    * @param query has a 500 character limit.
    * @param tweetCount has a default value of 100. For more use searchTweetHistory
    * @return A list of cleaned tweets from the given Query
    */
  def searchNRecentTweets(
    query: String,
    tweetCount: Int = count): Future[Seq[ShortTweetResponse]] = {
    searchForNTweets(query, tweetCount, ResultType.Recent)
  }

  /**
    *
    * @param query has a 500 character limit.
    * @param sinceID is the ID of the last gathered tweet
    * @return A list of cleaned tweets from the given Query
    */
  def searchSince(query: String, sinceID: Long): Future[Seq[ShortTweetResponse]] = {
    searchTweetsFrom(query, results = ResultType.Recent, sinceID = Some(sinceID))
  }

  /**
    * getUserTimelineFor is a general function to search the top 100 tweets of a specific users
    *   timeline
    *
    * This is a simple request the top 100 tweets and bam!
    *
    * @param ID of the user
    * @param tweetCount number of tweets to gather from a timeline
    * @return A list of cleaned tweets from the given Query
    */
  def fetchUserTimelineTop100Tweets(
    ID: Long,
    tweetCount: Integer = count): Future[Seq[ShortTweetResponse]] = {
    getUserTimelineFor(ID: Long, tweetCount)
  }
}

/** Companion object with a constructor that retrieves configurations */
object TweetScavenger {
  def apply()(implicit mapper: ObjectMapper): TweetScavenger = {
    new TweetScavenger()
  }
}
