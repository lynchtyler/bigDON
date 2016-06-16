package com.flashboomlet.db.implicits

import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONString
import reactivemongo.bson.BSONValue

/**
  * Created by trill on 6/14/16.
  */
trait MongoImplicits
  extends TweetImplicits
  with CountsImplicits
  with EntityImplicits
  with MetaDataImplicits
  with NewYorkTimesArticleImplicits
  with PreprocessDataImplicits
  with SentimentImplicits {

  /**
    * Converts a [[reactivemongo.bson.BSONDocument]] with the known structure of having [[String]]
    * for all keys and all values to a map.
    *
    * @param doc [[reactivemongo.bson.BSONDocument]] to convert to a [[Map]] with [[String]] keys
    *            and values
    * @return mapped [[reactivemongo.bson.BSONDocument]] with all keys and values as [[String]]s
    */
  private def bsonDocumentToMap(doc: BSONDocument): Map[String, String] = {
    // convert (String, BSONValue) to (String, String)
    val pairs = doc.elements.toSeq.map { (t: (String, BSONValue)) =>
      (t._1,
        t._2 match {
          case (x: BSONString) => x.value
          case _ => ""
        })
    }

    pairs.foldLeft[Map[String, String]](Map()) { (acc: Map[String, String], kv: (String, String)) =>
      acc + kv
    }
  }

  /**
    * Converts a map of k,v pairs into a [[reactivemongo.bson.BSONDocument]]
    *
    * @param attrMap The map with keys represented as strings, and values that
    *                intuitively have a toString method to convert into a
    *                [[reactivemongo.bson.BSONDocument]]
    * @return The newly created [[reactivemongo.bson.BSONDocument]]
    */
  private def mapToBSONDocument(attrMap: Map[String, Any]): BSONDocument = {
    attrMap.foldLeft[BSONDocument](BSONDocument()) { (doc: BSONDocument, kv: (String, Any)) =>
      doc.add(kv._1 -> BSONString(kv._2.toString))
    }
  }
}
