package com.flashboomlet.scavenger.twitter.configuration

import com.danielasfregola.twitter4s.entities.AccessToken
import com.danielasfregola.twitter4s.entities.ConsumerToken
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

class TwitterConfiguration(
  val consumerKey: String,
  val consumerSecret: String,
  val accesKey: String,
  val accessSecret: String) {

  def getConsumerToken: ConsumerToken = ConsumerToken(consumerKey, consumerSecret)
  def getAccessToken: AccessToken = AccessToken(accesKey, accessSecret)
}

object TwitterConfiguration {

  def apply(): TwitterConfiguration = {
    val config: Config = ConfigFactory.parseResources("twitter.conf")
    new TwitterConfiguration(
      config.getString("twitter.consumer.key"),
      config.getString("twitter.consumer.secret"),
      config.getString("twitter.access.key"),
      config.getString("twitter.access.secret")
    )

  }
}
