package com.flashboomlet.twitter

import java.text.SimpleDateFormat
import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities.enums.Language
import com.danielasfregola.twitter4s.entities.enums.ResultType
import com.danielasfregola.twitter4s.entities.enums.ResultType.ResultType
import com.danielasfregola.twitter4s.http.unmarshalling.CustomSerializers
import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.twitter.configuration.TwitterConfiguration
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
class TweetScavenger {

  final val count = 100
  final val quote = ","
  final val one = 1
  final val four = 4
  val twitterConfiguration = TwitterConfiguration()
  val client = new TwitterClient(twitterConfiguration.getConsumerToken,
    twitterConfiguration.getAccessToken)

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
    maxID: Option[Long] = None): Future[String] =  {

    client.getUserTimelineForUserId(
    user_id = ID,
    count = tweetCount,
    max_id = maxID).map { tweets =>
      toJson(tweets)
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
    * @return A String of the cleaned Query
    */
  private def searchForNTweets(
                              query: String,
                              tweetCount: Long = count,
                              results: ResultType = ResultType.Popular,
                              maxID: Option[Long] = None): Future[String] = {

    client.searchTweet(
      query = query,
      count = count,
      include_entities = false,
      result_type = results,
      language = Some(Language.English),
      max_id = maxID
    ).map { tweets =>
      val nextMaxId = extractNextMaxId(tweets.search_metadata.next_results)
      if ((count-100) <= 0)
      {
        toJson(tweets)
      } else {
        toJson(tweets) + searchForNTweets(
          query: String,
          tweetCount-100,
          results,
          nextMaxId)
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
    * @return A String of the cleaned Query
    */
  private def searchTweetsFrom(
                              query: String,
                              tweetCount: Long = count,
                              results: ResultType = ResultType.Popular,
                              sinceID: Option[Long] = None): Future[String] = {

    client.searchTweet(
      query = query,
      count = count,
      include_entities = false,
      result_type = results,
      language = Some(Language.English),
      since_id = sinceID
    ).map { tweets =>
      val resultCount = queriedCount(tweets.search_metadata.toString).toLong
      val nextSinceId = extractNextSinceId(tweets.search_metadata.toString)
      if (resultCount < 100)
      {
        toJson(tweets)
      } else {
        toJson(tweets) + searchForNTweets(
          query: String,
          count,
          results,
          nextSinceId)
      }
    }
  }

  // tweets.search_metadata.toString
  private def queriedCount(params: String): Long = {
    params.split(quote).takeRight(3).take(one).toString.toLong
  }

  private def extractNextMaxId(params: String): Option[Long] = {
    params.split("&").find(_.contains("max_id")).map(_.split("=")(one).toLong)
  }

  // tweets.search_metadata.toString
  private def extractNextSinceId(params: String): Option[Long] = {
    val s = params.split(quote).takeRight(four).take(one)
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
    * @param query
    * @param tweetCount
    * @return
    */
  def searchNPopularTweets(query: String, tweetCount: Int = count): Future[String] = {
    searchForNTweets(query, tweetCount)
  }

  /**
    *
    * @param query
    * @param tweetCount
    * @return
    */
  def searchNRecentTweets(query: String, tweetCount: Int = count): Future[String] = {
    searchForNTweets(query, tweetCount, ResultType.Recent)
  }

  /**
    *
    * @param query
    * @param sinceID
    * @return
    */
  def searchSince(query: String, sinceID: Long): Future[String] = {
    searchTweetsFrom(query, results = ResultType.Recent, sinceID = Some(sinceID))
  }

  /**
    * getUserTimelineFor is a general function to search the top 100 tweets of a specific users
    *   timeline
    *
    * This is a simple request the top 100 tweets and bam!
    *
    * @param ID
    * @param tweetCount
    * @return
    */
  def fetchUserTimelineTop100Tweets(ID: Long, tweetCount: Integer = count): Future[String] = {
    getUserTimelineFor(ID: Long, tweetCount)
  }
}

/** Companion object with a constructor that retrieves configurations */
object TweetScavenger {
  def apply()(implicit mapper: ObjectMapper): TweetScavenger = {
    new TweetScavenger()
  }
}
