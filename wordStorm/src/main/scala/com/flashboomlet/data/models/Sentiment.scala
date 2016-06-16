package com.flashboomlet.data.models

/**
  * The case class for the sentiment data model.
  *
  * @param sentiment the sentiment of the data
  * @param confidence the confidence of the sentiment
  */
case class Sentiment(
  sentiment: String,
  confidence: Float
)
