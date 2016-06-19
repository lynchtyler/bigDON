package com.flashboomlet.db.implicits

import com.flashboomlet.data.models.FinalTweet
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentWriter

/**
  * Implicit readers and writers for the tweet model in the tweets collection of MongoDB
  */
trait TweetImplicits {

  /** Implicit writer for MongoDB. */
  implicit object TweetWriter extends BSONDocumentWriter[FinalTweet] {

    override def write(tweet: FinalTweet): BSONDocument = {
      BSONDocument(
        // TODO
      )
    }
  }
}
