package com.flashboomlet.polls

import java.sql.Date

case class PollResponse(response: Response)

/**
  * Case class wrapping the articles
  *
  * @param chart The sequence of article "docs"
  */
case class Response(chart: Set[Chart])

case class Chart(
  id: Long,
  title: String,
  slug: String,
  topic: String,
  state: String,
  short_title: String,
  election_date: Date,
  poll_count: Long,
  last_updated: String,
  url: String,
  estimates: Array[Estimates],
  estimates_by_date: Array[Point]
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
  * Case class for the Estimates By Date's
  *
  * @param date
  * @param estimates
  */
case class Point (
  date: Date,
  estimates: List[DataPoint]
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
