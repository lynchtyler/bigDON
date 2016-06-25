package com.flashboomlet.data.models

/**
  * @param favoriteCount the amount of times the tweet was favorited at the time of harvest
  * @param retweetCount the amount of times the tweet was retweeted at the time of harvest
  */
case class TweetSocialData(
  retweetCount: Long,
  favoriteCount: Int,
  fetchDate: Long
)
