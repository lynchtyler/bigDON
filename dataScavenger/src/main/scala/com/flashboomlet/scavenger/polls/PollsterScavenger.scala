package com.flashboomlet.scavenger.polls

import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.data.models.Entity
import com.flashboomlet.data.models.MetaData
import com.flashboomlet.data.models.PollsterDataPoint
import com.flashboomlet.db.MongoDatabaseDriver
import com.flashboomlet.preproccessing.DateUtil.getToday
import com.flashboomlet.scavenger.Scavenger
import com.typesafe.scalalogging.LazyLogging

import scala.util.Try
import scalaj.http.Http
import scalaj.http.HttpRequest

/**
  * Created by ttlynch on 6/13/16.
  *
  * Class that fetches chart data from Huffington Posts Pollster API
  */
class PollsterScavenger(implicit val mapper: ObjectMapper,
    implicit val db: MongoDatabaseDriver) extends Scavenger with LazyLogging {

  final val ClintonIndex = 0

  final val TrumpIndex = 1

  final val OtherIndex = 2

  final val UndecidedIndex = 3

  /**
    * Scaffold for the scavengerTrait
    */
  def scavenge(entities: Set[Entity]): Unit = {
    Try {
      // Scavenge Chart
      val chart = scavengeChart()
      val today = getToday()
      val estimates = chart.estimates_by_date
      // Metadata
      val metaData = MetaData(
        fetchDate = today,
        publishDate = today,
        source = "Pollster",
        searchTerm = "N/A",
        entityLastName = "N/A", // TODO
        contributions = chart.poll_count.toInt
      )
      // Convert Chart Response to Chart Model
      val finalChart: List[PollsterDataPoint] = estimates.map { day =>
        com.flashboomlet.data.models.PollsterDataPoint(
          date = day.date.toString,
          clinton = day.estimates(ClintonIndex).value,
          trump = day.estimates(TrumpIndex).value,
          other = day.estimates(OtherIndex).value,
          undecided = day.estimates(UndecidedIndex).value,
          metaData = metaData
        )
      }.toList
      db.populateChart(finalChart)
      logger.info("Successfully populated pollster chart")
    }.getOrElse(
      logger.error("Failed to scavenge Pollster data.")
    )
  }

  /**
    * Goes and grabs a chart
    *
    * @param query a string for the search
    * @return a sequence of days or data points
    */
  private def scavengeChart(
    query: String = "2016-general-election-trump-vs-clinton"): Chart = {
    val BaseApiPath: String = "http://elections.huffingtonpost.com/pollster/api"

    val request: HttpRequest = Http(BaseApiPath + "/charts/" + query)
    val x = mapper.readValue(request.asBytes.body, classOf[Chart])
    com.flashboomlet.scavenger.polls.Chart(
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
}

/** Companion object with a constructor that retrieves configurations */
object PollsterScavenger {
  def apply()(implicit mapper: ObjectMapper, db: MongoDatabaseDriver): PollsterScavenger = {
    new PollsterScavenger()
  }
}
