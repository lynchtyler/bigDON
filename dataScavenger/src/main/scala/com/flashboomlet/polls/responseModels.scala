package com.flashboomlet.polls

import java.sql.Date

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import spray.http.DateTime

/**
  * Case class for top level SearchMetaData.
  *
  * @param response The actual metadata response
  */
case class SearchMetaData(response: MetaData)

/**
  * Case class for the meta data
  *
  * @param meta the wrapper
  */
@JsonIgnoreProperties(Array("days"))
case class MetaData(meta: Meta)

/**
  * Case class for the meta data
  *
  * @param id
  * @param title
  * @param slug
  * @param topic
  * @param state
  * @param short_title
  * @param election_date
  * @param poll_count
  * @param last_updated
  * @param url
  */
@JsonIgnoreProperties(Array("estimates"))
case class Meta(
  id: Long,
  title: String,
  slug: String,
  topic: String,
  state: String,
  short_title: String,
  election_date: Date,
  poll_count: Long,
  last_updated: DateTime,
  url: String
)

/**
  * Case class for the Estimates data
  *
  * @param choice
  * @param value
  * @param lead_confidence
  * @param first_name
  * @param last_name
  * @param party
  * @param incumbent
  */
case class Estimates (
  choice: String,
  value: Float,
  lead_confidence: Float,
  first_name: String,
  last_name: String,
  party: String,
  incumbent: Boolean
)

/**
  * Case class for article response model. Excludes meta data
  *
  * @param response Wraps the response
  */
@JsonIgnoreProperties(Array(
  "id",
  "title",
  "slug",
  "topic",
  "state",
  "short_title",
  "election_date",
  "poll_count",
  "last_updated",
  "url",
  "estimates",
  "estimates_by_date"))
case class PollResponse(response: Response)

/**
  * Case class wrapping the Estimates By Date
  *
  * @param days The sequence of article "docs"
  */
@JsonIgnoreProperties(Array("meta"))
case class Response(days: Set[Day])

/**
  * Case class for the Estimates By Date's
  *
  * @param date
  * @param estimates
  */
case class Day (
  date: Date,
  estimates: Seq[DataPoint]
)

/**
  * Case class for the DataPoint's
  *
  * @param choice
  * @param value
  */
case class DataPoint (
  choice: String,
  value: Float
)

/**
  * Case class for article response model. Excludes meta data
  *
  * @param estimatesResponse Wraps the response
  */
case class EstimateResponse(estimatesResponse: EstimatesResponse)

/**
  * Case class wrapping the Estimates By Date
  *
  * @param dataPoints The sequence of article "docs"
  */
@JsonIgnoreProperties(Array("meta"))
case class EstimatesResponse(dataPoints: Set[DataPoint])