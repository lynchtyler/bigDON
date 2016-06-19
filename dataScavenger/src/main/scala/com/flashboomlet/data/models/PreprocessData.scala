package com.flashboomlet.data.models

/**
  * Case class for the Pre-process data model.
  *
  * @param sentiment the sentiment data model
  * @param counts the counts data model
  */
case class PreprocessData(
  sentiment: Sentiment,
  counts: Counts
)
