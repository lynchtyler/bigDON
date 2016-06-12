package com.flashboomlet.preproccessing

/** Case class for wrapping a sentiment analysis result from the FastSentimentClassifier */
case class SentimentAnalysis(result: SentimentData)

/** Case class for the actual data being wrapped by the result in the fast sentiment classifier */
case class SentimentData(sentiment: String, confidence: Float)
