package com.flashboomlet.polls

import com.fasterxml.jackson.databind.ObjectMapper
import scalaj.http.Http
import scalaj.http.HttpRequest

/**
  * Created by ttlynch on 6/13/16.
  */
class PollsterScavenger(implicit val mapper: ObjectMapper) {

  /**
    * Goes and grabs a chart
    *
    * @param query
    * @return
    */
  def scavengeChart(query: String = "2016-general-election-trump-vs-clinton"): Unit = {
    // Seq[Day] = {
    val BaseApiPath: String = "http://elections.huffingtonpost.com/pollster/api"

    val request: HttpRequest = Http(BaseApiPath + "/charts/" + query)
    /*
    mapper.readValue(request.asBytes.body, classOf[PollResponse])
    .response.days.toSeq.map { (day: Day) =>
      Day(
        date = day.date,
        estimates = getDatePoints(request)
      )
    }
    */
  }

  /**
    * Searches for and fetches all of the data points for a day
    *
    * @param request
    * @return
    */
  def getDatePoints(request: HttpRequest): Seq[DataPoint] = {
    mapper.readValue(request.asBytes.body, classOf[EstimateResponse])
    .estimatesResponse.dataPoints.toSeq.map { (dataPoint: DataPoint) =>
      DataPoint(
        choice = dataPoint.choice,
        value = dataPoint.value
      )
    }
  }

}

/** Companion object with a constructor that retrieves configurations */
object PollsterScavenger {
  def apply()(implicit mapper: ObjectMapper): PollsterScavenger = {
    new PollsterScavenger()
  }
}

