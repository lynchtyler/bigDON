package com.flashboomlet.scavenger.polls

import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.polls.Chart
import com.flashboomlet.scavenger.scavenger

import scalaj.http.Http
import scalaj.http.HttpRequest

/**
  * Created by ttlynch on 6/13/16.
  *
  * Class that fetches chart data from Huffington Posts Pollster API
  */
class PollsterScavenger(implicit val mapper: ObjectMapper) extends scavenger {

  /**
    * Goes and grabs a chart
    *
    * @param query a string for the search
    * @return a sequence of days or data points
    */
  def scavengeChart(
    query: String = "2016-general-election-trump-vs-clinton"): Chart = {
    val BaseApiPath: String = "http://elections.huffingtonpost.com/pollster/api"

    val request: HttpRequest = Http(BaseApiPath + "/charts/" + query)
    val x = mapper.readValue(request.asBytes.body, classOf[Chart])
    Chart(
      id = x.id,
      title = x.title,
      slug = x.slug,
      topic = x.topic,
      state = x.state,
      short_title = x.short_title,
      election_date = x.election_date,
      poll_count = x.poll_count,
      last_updated = x.last_updated,
      url = x.url,
      estimates = x.estimates,
      estimates_by_date = x.estimates_by_date
    )
  }

  /**
    * Scaffold for the scavengerTrait
    */
  def scavenge(): Unit = {}
}

/** Companion object with a constructor that retrieves configurations */
object PollsterScavenger {
  def apply()(implicit mapper: ObjectMapper): PollsterScavenger = {
    new PollsterScavenger()
  }
}
