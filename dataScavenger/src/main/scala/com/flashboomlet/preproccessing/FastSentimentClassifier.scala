package com.flashboomlet.preproccessing

import com.fasterxml.jackson.databind.ObjectMapper

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scalaj.http.Http


/** Object containing REST call to analyze sentiment of text */
object FastSentimentClassifier {

  private[this] val RequestLocation = "http://sentiment.vivekn.com/api/text/"

  /**
    * Gets the sentiment of a piece of text by querient the fast sentiment classifier rest api.
    *
    * @param text Text to be analyzed
    * @param mapper Object mapper to parse JSON response
    * @return Future sentiment Data
    */
  def getSentiment(text: String)(implicit mapper: ObjectMapper): Future[SentimentData] = {
    Future {
      mapper.readValue(
        Http(RequestLocation).postForm(Seq("txt" -> text)).asBytes.body,
        classOf[SentimentAnalysis]).result
    }
  }
}
