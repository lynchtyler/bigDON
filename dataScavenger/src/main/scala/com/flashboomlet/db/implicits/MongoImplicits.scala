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
  with SentimentImplicits
  with PollsterDataPointImplicits
