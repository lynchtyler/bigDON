package com.flashboomlet.db.implicits

import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.Counts
import com.flashboomlet.data.models.PreprocessData
import com.flashboomlet.data.models.Sentiment
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter

/** Implicit readers and writers for the preprocessing data model in MongoDB */
trait PreprocessDataImplicits
  extends MongoConstants
  with CountsImplicits
  with SentimentImplicits {

  /** Implicit writer for PreproccessData */
  implicit object PreprocessDataWriter extends BSONDocumentWriter[PreprocessData] {

    override def write(preprocessData: PreprocessData): BSONDocument = BSONDocument(
      PreprocessDataConstants.CountsString -> preprocessData.counts,
      PreprocessDataConstants.SentimentString -> preprocessData.sentiment
    )
  }

  implicit object PreprocessDataReader extends BSONDocumentReader[PreprocessData] {

    override def read(doc: BSONDocument): PreprocessData = {
      val sentiment = doc.getAs[Sentiment](PreprocessDataConstants.SentimentString).get
      val counts = doc.getAs[Counts](PreprocessDataConstants.CountsString).get

      PreprocessData(sentiment, counts)
    }
  }
}
