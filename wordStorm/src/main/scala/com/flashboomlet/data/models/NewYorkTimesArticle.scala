package com.flashboomlet.data.models

/**
  * Created by trill on 6/14/16.
  */
case class NewYorkTimesArticle(
  url: String,
  title: String,
  author: String,
  body: String,
  summaries: Set[String],
  keyPeople: Set[String],
  metaData: MetaData,
  preprocessData: PreprocessData
)
