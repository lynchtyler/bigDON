package com.flashboomlet.preproccessing

import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.data.models.Sentiment

import scalaj.http.Http


/** Object containing REST call to analyze sentiment of text */
object FastSentimentClassifier {

  private[this] val RequestLocation = "http://172.16.238.10:8081/web/text/"

  /**
    * Gets the sentiment of a piece of text by querient the fast sentiment classifier rest api.
    *
    * @param text Text to be analyzed
    * @param mapper Object mapper to parse JSON response
    * @return Future sentiment Data
    */
  def getSentiment(text: String)(implicit mapper: ObjectMapper): Sentiment = {
    val request = Http(RequestLocation).postForm(Seq("txt" -> text)).asString.body
    mapper.readValue(request,classOf[Sentiment])
  }
}
