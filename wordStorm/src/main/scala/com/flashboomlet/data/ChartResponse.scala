package com.flashboomlet.data

case class ChartResponse(
  id: String,
  pollster: String,
  start_date: String,
  end_date: String,
  method: String,
  source: String,
  questions: String,
  survey_houses: String,
  sponsors: String,
  partisan: String,
  affiliation: String
)
