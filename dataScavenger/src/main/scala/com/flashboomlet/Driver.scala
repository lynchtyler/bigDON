package com.flashboomlet

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.flashboomlet.articles.nyt.NewYorkTimesScavenger
import com.flashboomlet.twitter.TweetScavenger
import com.flashboomlet.polls.PollsterScavenger

/** Entry point to the data scavenger program */
object Driver {

  final val count = 100
  final val USALynch = "2413302566"

  /** Defines single global instance of JSON object mapper that can be used throughout. */
  implicit val objectMapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
      .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)

  /** Main entry point to the program */
  def main(args: Array[String]): Unit = {
    val newYorkTimesScavenger: NewYorkTimesScavenger = NewYorkTimesScavenger()
    val pollsterScavenger: PollsterScavenger = PollsterScavenger()
    val tweetScavenger: TweetScavenger = TweetScavenger()

    // fetch newYorkTimesScavenger.scavengeArticles(...) here! . . . or start a chron job
    // tweetScavenger.searchNRecentTweets("#Trump", count)
    // tweetScavenger.fetchUserTimelineTop100Tweets(USALynch.toLong, count)
    // pollsterScavenger.scavengeChart()
  }
}
