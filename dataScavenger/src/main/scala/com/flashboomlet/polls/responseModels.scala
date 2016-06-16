package com.flashboomlet.polls

import java.sql.Date

case class PollResponse(response: Response)

/**
  * Case class wrapping the articles
  *
  * @param chart The sequence of article "docs"
  */
case class Response(chart: Set[Chart])

/**
  * The Data Response by the Pollster API
  *
  * @param id id of the chart
  * @param title title of the chart
  * @param slug the search term
  * @param topic the topic of the chart
  * @param state the location that the chart responds to
  * @param short_title the shortened title
  * @param election_date the date of the election
  * @param poll_count the amount of polls that the chart is derived from
  * @param last_updated the date that the data was last updated
  * @param url of the GUI version of the chart
  * @param estimates the meta data for each data point estimate
  * @param estimates_by_date the actual dates and the values corresponding to each
  */
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
  * @param choice the choice tag line ("Last Name", Other)
  * @param value current value
  * @param lead_confidence the confidence that the choice is leading
  * @param first_name the choice first name ("Name", null)
  * @param last_name the choice last name ("Name", null)
  * @param party the party of the choice (Dem, Rep, N/A)
  * @param incumbent boolean for whether the choice is the incumbent
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
