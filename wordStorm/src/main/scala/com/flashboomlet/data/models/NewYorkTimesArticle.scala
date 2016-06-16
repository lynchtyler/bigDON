package com.flashboomlet.data.models

/**
  * Case class for the New York Times Article data model.
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
