package com.flashboomlet.data.models

/**
  *  Case class of the tweet data model
  *
  * @param tweetID id of the tweet
  * @param content the contents of the tweet
  * @param socialDatas the sequence of social data related to time
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
  country: String,
  isRetweet: Boolean,
  parentTweetId: Long,
  socialDatas: Set[TweetSocialData],
  metaDatas: Set[MetaData],
  preprocessData: PreprocessData
)
