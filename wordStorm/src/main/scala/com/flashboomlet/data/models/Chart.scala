package com.flashboomlet.data.models

import java.sql.Date

/**
  * Case class for the Estimates By Date's
  *
  * @param date data point date
  * @param clinton the value for clinton
  * @param trump the value for trump
  * @param other the value for other
  * @param undecided the value for undecided
  */
case class Chart(
  date: Date,
  clinton: Float,
  trump: Float,
  other: Float,
  undecided: Float
)
