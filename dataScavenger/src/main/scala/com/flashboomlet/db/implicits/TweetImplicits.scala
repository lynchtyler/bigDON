package com.flashboomlet.db.implicits

import com.flashboomlet.data.models.FinalTweet
import com.flashboomlet.data.models.MetaData
import com.flashboomlet.data.models.PreprocessData
import com.flashboomlet.data.models.TweetSocialData
import com.flashboomlet.db.MongoConstants
import reactivemongo.bson.BSONBoolean
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONInteger
import reactivemongo.bson.BSONLong
import reactivemongo.bson.BSONString

/**
  * Implicit readers and writers for the tweet model in the tweets collection of MongoDB
  */
trait TweetImplicits
  extends MongoConstants
  with TweetSocialDataImplicits
  with MetaDataImplicits
  with PreprocessDataImplicits {

  /** Implicit writer for MongoDB. */
  implicit object TweetWriter extends BSONDocumentWriter[FinalTweet] {

    override def write(tweet: FinalTweet): BSONDocument = {
      BSONDocument(
        TwitterConstants.TweetIDString -> BSONLong(tweet.tweetID),
        TwitterConstants.ContentString -> BSONString(tweet.content),
        TwitterConstants.FollowersCountString -> BSONInteger(tweet.followersCount),
        TwitterConstants.FriendsCountString -> BSONInteger(tweet.friendsCount),
        TwitterConstants.UserIDString -> BSONLong(tweet.userID),
        TwitterConstants.NameString -> BSONString(tweet.name),
        TwitterConstants.ScreenNameString -> BSONString(tweet.screenName),
        TwitterConstants.CountryString -> BSONString(tweet.country),
        TwitterConstants.ParentTweetIdString -> BSONLong(tweet.parentTweetId),
        TwitterConstants.IsRetweetString -> BSONBoolean(tweet.isRetweet),
        TwitterConstants.SocialDatasString -> tweet.socialDatas,
        GlobalConstants.MetaDatasString -> tweet.metaDatas,
        GlobalConstants.PreprocessDataString -> tweet.preprocessData
      )
    }
  }

  /** Implicit reader for Entity class */
  implicit object TweetReader extends BSONDocumentReader[FinalTweet] {

    override def read(doc: BSONDocument): FinalTweet = {
      val tweetID = doc.getAs[Long](TwitterConstants.TweetIDString).get
      val content = doc.getAs[String](TwitterConstants.ContentString).get
      val followersCount = doc.getAs[Int](TwitterConstants.FollowersCountString).get
      val friendsCount = doc.getAs[Int](TwitterConstants.FriendsCountString).get
      val userID = doc.getAs[Long](TwitterConstants.UserIDString).get
      val name = doc.getAs[String](TwitterConstants.NameString).get
      val screenName = doc.getAs[String](TwitterConstants.ScreenNameString).get
      val country = doc.getAs[String](TwitterConstants.CountryString).get
      val parentTweetId = doc.getAs[Long](TwitterConstants.ParentTweetIdString).get
      val isRetweet = doc.getAs[Boolean](TwitterConstants.IsRetweetString).get
      val socialDatas = doc.getAs[Set[TweetSocialData]](TwitterConstants.SocialDatasString).get
      val metaData = doc.getAs[Set[MetaData]](GlobalConstants.MetaDatasString).get
      val preprocessData = doc.getAs[PreprocessData](GlobalConstants.PreprocessDataString).get


      FinalTweet(
        tweetID = tweetID,
        content = content,
        followersCount = followersCount,
        friendsCount = friendsCount,
        userID = userID,
        name = name,
        screenName = screenName,
        country = country,
        isRetweet = isRetweet,
        parentTweetId = parentTweetId,
        socialDatas = socialDatas,
        metaDatas = metaData,
        preprocessData = preprocessData
      )
    }
  }
}
