package com.flashboomlet.data.models

/**
  * Case class for the actual data being wrapped by the result in the fast sentiment classifier.
  * Doubles as a DB model
  *
  * @param sentiment the sentiment of the data
  * @param confidence the confidence of the sentiment
  */
case class Sentiment(
  sentiment: String,
  confidence: Float
)
