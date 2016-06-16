package com.flashboomlet.db.implicits

import com.flashboomlet.data.models.Tweet
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentWriter

/**
  * Implicit readers and writers for the tweet model in the tweets collection of MongoDB
  */
trait TweetImplicits {

  /** Implicit writer for MongoDB. */
  implicit object TweetWriter extends BSONDocumentWriter[Tweet] {

    override def write(tweet: Tweet): BSONDocument = {
      BSONDocument(
        // TODO
      )
    }
  }
}
