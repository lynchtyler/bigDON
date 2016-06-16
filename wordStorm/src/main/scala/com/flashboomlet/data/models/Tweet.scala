package com.flashboomlet.data.models

import java.util.Date

/**
  *  Case class of the tweet data model
  *
  * @param tweet_id id of the tweet
  * @param text the contents of the tweet
  * @param created_at the date and time that the tweet was created
  * @param favorite_count the amount of times the tweet was favorited at the time of harvest
  * @param retweet_count the amount of times the tweet was retweeted at the time of harvest
  * @param country is the country of where the tweet was tweeted from
  * @param user_id the id of the user
  * @param followers_count the followers count that the user has
  * @param friends_count the firends count that the user is following
  */
case class Tweet(
  tweet_id: Long,
  text: String,
  followers_count: Int,
  friends_count: Int,
  user_id: Long,
  name: String,
  screen_name: String,
  created_at: Date,
  favorite_count: Int = 0,
  country: String,
  retweet_count: Long = 0
)
