package com.flashboomlet.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
  * Case class for the actual data being wrapped by the result in the fast sentiment classifier.
  * Doubles as a DB model
  *
  * @param result the sentiment of the data
  * @param confidence the confidence of the sentiment
  */
@JsonIgnoreProperties(Array("sentence"))
case class Sentiment(
  result: String,
  confidence: String
)
