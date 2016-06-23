package com.flashboomlet.data.models

/**
  * Case class for the Estimates By Date's
  *
  * @param date data point date  YYYY-MM-DD
  * @param clinton the value for clinton
  * @param trump the value for trump
  * @param other the value for other
  * @param undecided the value for undecided
  */
case class PollsterDataPoint(
  date: Long,
  clinton: Double,
  trump: Double,
  other: Double,
  undecided: Double,
  metaData: MetaData
)
