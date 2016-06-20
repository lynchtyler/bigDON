package com.flashboomlet.db.implicits

import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.TwitterSearch
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONLong
import reactivemongo.bson.BSONString

/**
  * Implicit readers and writers for the tweet search model in the tweets searches collection
  */
trait TwitterSearchImplicits extends MongoConstants {

  /** Implicit writer for the twitter search class model */
  implicit object TwitterSearchWriter extends BSONDocumentWriter[TwitterSearch] {

    override def write(twitterSearch: TwitterSearch): BSONDocument = BSONDocument(
      TwitterSearchConstants.QueryString -> BSONString(twitterSearch.query),
      TwitterSearchConstants.EntityLastNameString -> BSONString(twitterSearch.entityLastName),
      TwitterSearchConstants.RecentTwitterIdString -> BSONLong(twitterSearch.recentTwitterId)
    )
  }

  /** Implicit reader for the twitter search class model */
  implicit object TwitterSearchReader extends BSONDocumentReader[TwitterSearch] {

    override def read(doc: BSONDocument): TwitterSearch = {
      val query = doc.getAs[String](TwitterSearchConstants.QueryString).get
      val entityLastName = doc.getAs[String](TwitterSearchConstants.EntityLastNameString).get
      val recentTwitterId = doc.getAs[Long](TwitterSearchConstants.RecentTwitterIdString).get

      TwitterSearch(
        query = query,
        entityLastName = entityLastName,
        recentTwitterId = recentTwitterId
      )
    }
  }
}
