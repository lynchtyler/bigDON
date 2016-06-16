package com.flashboomlet.db.implicits

import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.MetaData
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONString

/** Implicit readers and writers for theMEtaData model in MongoDB */
trait MetaDataImplicits extends MongoConstants {

  /** Implicit writer for the MetaData class */
  implicit object MetaDataWriter extends BSONDocumentWriter[MetaData] {

    override def write(metaData: MetaData): BSONDocument = BSONDocument(
      MetaDataConstants.FetchDateString -> BSONString(metaData.fetchDate),
      MetaDataConstants.PublishDateString -> BSONString(metaData.publishDate),
      MetaDataConstants.SourceString -> BSONString(metaData.source),
      MetaDataConstants.SearchTermString -> BSONString(metaData.searchTerm),
      MetaDataConstants.EntityIdString -> BSONString(metaData.entityId)
    )
  }

  /** Implicit reader for the MetaData class */
  implicit object MetaDataReader extends BSONDocumentReader[MetaData] {

    override def read(doc: BSONDocument): MetaData = {
      val fetchDate = doc.getAs[String](MetaDataConstants.FetchDateString).get
      val publishDate = doc.getAs[String](MetaDataConstants.PublishDateString).get
      val source = doc.getAs[String](MetaDataConstants.SourceString).get
      val searchTerm = doc.getAs[String](MetaDataConstants.SearchTermString).get
      val entityId = doc.getAs[String](MetaDataConstants.EntityIdString).get

      MetaData(
        fetchDate = fetchDate,
        publishDate = publishDate,
        source = source,
        searchTerm = searchTerm,
        entityId = entityId
      )
    }
  }
}
