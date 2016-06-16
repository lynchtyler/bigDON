package com.flashboomlet.db.implicits

import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.Sentiment
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONDouble
import reactivemongo.bson.BSONString

/** Implicit readers and writers for the Sentiment model in of MongoDB */
trait SentimentImplicits extends MongoConstants {

  /** Implicit conversion object for Sentiment writing */
  implicit object SentimentWriter extends BSONDocumentWriter[Sentiment] {

    override def write(sentiment: Sentiment): BSONDocument = BSONDocument(
      SentimentConstants.SentimentString -> BSONString(sentiment.sentiment),
      SentimentConstants.ConfidenceString -> BSONDouble(sentiment.confidence)
    )
  }


  /** Implicit conversion object for reading Sentiment class */
  implicit object SentimentReader extends BSONDocumentReader[Sentiment] {

    override def read(doc: BSONDocument): Sentiment = {
      val sentiment = doc.getAs[String](SentimentConstants.SentimentString).get
      val confidence = doc.getAs[Double](SentimentConstants.ConfidenceString).get

      Sentiment(sentiment, confidence.asInstanceOf[Float])
    }
  }
}
