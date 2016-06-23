package com.flashboomlet.db.implicits

import com.flashboomlet.data.models.Sentiment
import com.flashboomlet.db.MongoConstants
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONString

/** Implicit readers and writers for the Sentiment model in of MongoDB */
trait SentimentImplicits extends MongoConstants {

  /** Implicit conversion object for Sentiment writing */
  implicit object SentimentWriter extends BSONDocumentWriter[Sentiment] {

    override def write(sentiment: Sentiment): BSONDocument = BSONDocument(
      SentimentConstants.ResultString -> BSONString(sentiment.result),
      SentimentConstants.ConfidenceString -> BSONString(sentiment.confidence)
    )
  }


  /** Implicit conversion object for reading Sentiment class */
  implicit object SentimentReader extends BSONDocumentReader[Sentiment] {

    override def read(doc: BSONDocument): Sentiment = {
      val sentiment = doc.getAs[String](SentimentConstants.ResultString).get
      val confidence = doc.getAs[String](SentimentConstants.ConfidenceString).get

      Sentiment(sentiment, confidence)
    }
  }
}
