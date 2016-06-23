package com.flashboomlet.db.implicits

import java.util.Date

import com.flashboomlet.data.models.MetaData
import com.flashboomlet.db.MongoConstants
import reactivemongo.bson.BSONDateTime
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONInteger
import reactivemongo.bson.BSONString

/** Implicit readers and writers for theMEtaData model in MongoDB */
trait MetaDataImplicits extends MongoConstants {

  /** Implicit writer for the MetaData class */
  implicit object MetaDataWriter extends BSONDocumentWriter[MetaData] {

    override def write(metaData: MetaData): BSONDocument = BSONDocument(
      MetaDataConstants.FetchDateString -> BSONDateTime(metaData.fetchDate),
      MetaDataConstants.PublishDateString -> BSONDateTime(metaData.publishDate),
      MetaDataConstants.SourceString -> BSONString(metaData.source),
      MetaDataConstants.SearchTermString -> BSONString(metaData.searchTerm),
      MetaDataConstants.EntityLastNameString -> BSONString(metaData.entityLastName),
      MetaDataConstants.ContributionsString -> BSONInteger(metaData.contributions)
    )
  }

  /** Implicit reader for the MetaData class */
  implicit object MetaDataReader extends BSONDocumentReader[MetaData] {

    override def read(doc: BSONDocument): MetaData = {
      val fetchDate = doc.getAs[Date](MetaDataConstants.FetchDateString).get.getTime
      val publishDate = doc.getAs[Date](MetaDataConstants.PublishDateString).get.getTime
      val source = doc.getAs[String](MetaDataConstants.SourceString).get
      val searchTerm = doc.getAs[String](MetaDataConstants.SearchTermString).get
      val entityLastName = doc.getAs[String](MetaDataConstants.EntityLastNameString).get
      val contributions = doc.getAs[Int](MetaDataConstants.ContributionsString).getOrElse(0)

      MetaData(
        fetchDate = fetchDate,
        publishDate = publishDate,
        source = source,
        searchTerm = searchTerm,
        entityLastName = entityLastName,
        contributions = contributions
      )
    }
  }
}
