package com.flashboomlet.db.implicits

import java.util.Date

import com.flashboomlet.data.models.TweetSocialData
import com.flashboomlet.db.MongoConstants
import reactivemongo.bson.BSONDateTime
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONInteger
import reactivemongo.bson.BSONLong

/**
  * Created by trill on 6/24/16.
  */
trait TweetSocialDataImplicits extends MongoConstants {
  /** Implicit writer for MongoDB. */
  implicit object TweetSocialDataWriter extends BSONDocumentWriter[TweetSocialData] {

    override def write(tsd: TweetSocialData): BSONDocument = {
      BSONDocument(
        TweetSocialDataConstants.FavoriteCountString -> BSONInteger(tsd.favoriteCount),
        TweetSocialDataConstants.RetweetCountString -> BSONLong(tsd.retweetCount),
        TweetSocialDataConstants.FetchedDateString -> BSONDateTime(tsd.fetchDate)
      )
    }
  }

  /** Implicit reader for Entity class */
  implicit object TweetSocialDataReader extends BSONDocumentReader[TweetSocialData] {

    override def read(doc: BSONDocument): TweetSocialData = {
      val fetchDate = doc.getAs[Date](TweetSocialDataConstants.FetchedDateString).get.getTime
      val retweetCount = doc.getAs[Long](TweetSocialDataConstants.RetweetCountString).get
      val favoriteCount = doc.getAs[Int](TweetSocialDataConstants.FavoriteCountString).get

      TweetSocialData(
        fetchDate = fetchDate,
        retweetCount = retweetCount,
        favoriteCount = favoriteCount
      )
    }
  }
}
