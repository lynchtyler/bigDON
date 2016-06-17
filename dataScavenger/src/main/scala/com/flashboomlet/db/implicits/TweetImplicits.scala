package com.flashboomlet.db.implicits

<<<<<<< Updated upstream
import com.flashboomlet.data.models.Tweet
=======
import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.FinalTweet
import com.flashboomlet.data.models.MetaData
import com.flashboomlet.data.models.PreprocessData
>>>>>>> Stashed changes
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONInteger
import reactivemongo.bson.BSONLong
import reactivemongo.bson.BSONString

/**
  * Implicit readers and writers for the tweet model in the tweets collection of MongoDB
  */

/** Implicit readers and writers for the preprocessing data model in MongoDB */
trait TweetImplicits
  extends MongoConstants
  with MetaDataImplicits
  with PreprocessDataImplicits {

  /** Implicit writer for MongoDB. */
  implicit object TweetWriter extends BSONDocumentWriter[Tweet] {

    override def write(tweet: Tweet): BSONDocument = {
      BSONDocument(
        TwitterConstants.tweetIDString -> BSONLong(tweet.tweetID),
        TwitterConstants.contentString -> BSONString(tweet.content),
        TwitterConstants.followersCountString -> BSONInteger(tweet.followersCount),
        TwitterConstants.friendsCountString -> BSONInteger(tweet.friendsCount),
        TwitterConstants.friendsCountString -> BSONLong(tweet.userID),
        TwitterConstants.friendsCountString -> BSONString(tweet.name),
        TwitterConstants.friendsCountString -> BSONString(tweet.screenName),
        TwitterConstants.friendsCountString -> BSONInteger(tweet.favoriteCount),
        TwitterConstants.friendsCountString -> BSONString(tweet.country),
        TwitterConstants.friendsCountString -> BSONLong(tweet.retweetCount),
        TwitterConstants.friendsCountString -> tweet.metaData,
        TwitterConstants.friendsCountString -> tweet.preprocessData
      )
    }
  }

  /** Implicit reader for Entity class */
  implicit object TweetReader extends BSONDocumentReader[FinalTweet] {

    override def read(doc: BSONDocument): FinalTweet = {
      val tweetID = doc.getAs[Long](TwitterConstants.tweetIDString).get
      val content = doc.getAs[String](TwitterConstants.contentString).get
      val followersCount = doc.getAs[Int](TwitterConstants.followersCountString).get
      val friendsCount = doc.getAs[Int](TwitterConstants.friendsCountString).get
      val userID = doc.getAs[Long](TwitterConstants.userIDString).get
      val name = doc.getAs[String](TwitterConstants.nameString).get
      val screenName = doc.getAs[String](TwitterConstants.screenNameString).get
      val favoriteCount = doc.getAs[Int](TwitterConstants.favoriteCountString).get
      val country = doc.getAs[String](TwitterConstants.countryString).get
      val retweetCount = doc.getAs[Long](TwitterConstants.retweetCountString).get
      val metaData = doc.getAs[MetaData](GlobalConstants.MetaDataString).get
      val preprocessData = doc.getAs[PreprocessData](GlobalConstants.PreprocessDataString).get

      FinalTweet(
        tweetID = tweetID,
        content = content,
        followersCount = followersCount,
        friendsCount = friendsCount,
        userID = userID,
        name = name,
        screenName = screenName,
        favoriteCount = favoriteCount,
        country = country,
        retweetCount = retweetCount,
        metaData = metaData,
        preprocessData = preprocessData
      )

    }
  }
}
