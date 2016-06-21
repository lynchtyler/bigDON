package com.flashboomlet.db

import reactivemongo.bson.BSONDocument
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
  def bsonDocumentToMap(doc: BSONDocument): Map[String, String] = {
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
  def mapToBSONDocument(attrMap: Map[String, Any]): BSONDocument = {
    attrMap.foldLeft[BSONDocument](BSONDocument()) { (doc: BSONDocument, kv: (String, Any)) =>
      doc.add(kv._1 -> BSONString(kv._2.toString))
    }
  }

  def getOptionalString(option: Option[String]): String = {
    option match {
      case Some(s) => s
      case None => ""
    }
  }

  def getOptionalSet(option: Option[Set[String]]): Set[String] = {
    option match {
      case Some(s) => s
      case None => Set()
    }
  }
}
