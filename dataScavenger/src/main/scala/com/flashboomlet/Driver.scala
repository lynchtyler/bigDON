package com.flashboomlet

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.flashboomlet.db.MongoDatabaseDriver
import com.flashboomlet.scavenger.NewYorkTimesScavenger
import com.flashboomlet.scavenger.PollsterScavenger
import com.flashboomlet.scavenger.TweetScavenger

/** Entry point to the data scavenger program */
object Driver {
  /** Defines single global instance of JSON object mapper that can be used throughout. */
  implicit val objectMapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
      .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)

  /** Database driver to be used globally throughout. */
  implicit val databaseDriver: MongoDatabaseDriver = MongoDatabaseDriver()

  /** Main entry point to the program */
  def main(args: Array[String]): Unit = {
    val newYorkTimesScavenger: NewYorkTimesScavenger = NewYorkTimesScavenger()
    val pollsterScavenger: PollsterScavenger = PollsterScavenger()
    val tweetScavenger: TweetScavenger = TweetScavenger()


    // fetch newYorkTimesScavenger.scavengeArticles(...) here! . . . or start a chron job
    // tweetScavenger.searchNRecentTweets("#Trump")
    // tweetScavenger.fetchUserTimelineTop100Tweets(("2413302566").toLong) // @USALynch 's timeline
    // pollsterScavenger.scavengeChart()
  }
}
