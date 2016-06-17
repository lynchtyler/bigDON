package com.flashboomlet.data.models

/**
  *  Case class of the tweet data model
  *
  * @param tweetID id of the tweet
  * @param content the contents of the tweet
  * @param favoriteCount the amount of times the tweet was favorited at the time of harvest
  * @param retweetCount the amount of times the tweet was retweeted at the time of harvest
  * @param country is the country of where the tweet was tweeted from
  * @param userID the id of the user
  * @param followersCount the followers count that the user has
  * @param friendsCount the firends count that the user is following
  */
case class FinalTweet(
  tweetID: Long,
  content: String,
  followersCount: Int,
  friendsCount: Int,
  userID: Long,
  name: String,
  screenName: String,
  favoriteCount: Int,
  country: String,
  retweetCount: Long,
  metaData: MetaData,
  preprocessData: PreprocessData
)
