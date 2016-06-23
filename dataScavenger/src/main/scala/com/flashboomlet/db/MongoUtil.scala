package com.flashboomlet.db

import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONInteger
import reactivemongo.bson.BSONString
import reactivemongo.bson.BSONValue

/**
  * Created by ttlynch on 6/16/16.
  */
object MongoUtil {

  /**
    * Converts a [[reactivemongo.bson.BSONDocument]] with the known structure of having [[String]]
    * for all keys and all values to a map.
    *
    * @param doc [[reactivemongo.bson.BSONDocument]] to convert to a [[Map]] with [[String]] keys
    *            and values
    * @return mapped [[reactivemongo.bson.BSONDocument]] with all keys and values as [[String]]s
    */
  def bsonDocumentToMap(doc: BSONDocument): Map[String, Int] = {
    // convert (String, BSONValue) to (String, Int)
    val pairs: Seq[(String, Int)] = doc.elements.map { (t: (String, BSONValue)) =>
      (t._1,
      t._2 match {
        case (x: BSONInteger) => x.value
        case _ => 0
      })
    }

    pairs.foldLeft[Map[String, Int]](Map[String, Int]()) {
      (acc: Map[String, Int], kv: (String, Int)) => acc + kv
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
  def mapToBSONDocument(attrMap: Map[String, Int]): BSONDocument = {
    attrMap.foldLeft[BSONDocument](BSONDocument()) { (doc: BSONDocument, kv: (String, Int)) =>
      doc.add(kv._1 -> BSONInteger(kv._2))
    }
  }
}
