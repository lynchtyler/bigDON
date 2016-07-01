package com.flashboomlet.preproccessing

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
  * DateUtil is used to handle the date conversions from various sources into the proper
  * mongoDB Date/Time format to conform to the BSON ISODate format
  *
  * Converts all dates to ISO format in a normalized standard using the UTC time standard.
  *
  * http://stackoverflow.com/questions/3778428/best-way-to-store-date-time-in-mongodb
  *
  */
object DateUtil {

  private val NytQueryDateFormat = "yyyyMMdd"

  def getPollsterInMillis(pollsterDate: Date): Long = pollsterDate.getTime

  def getTweetInMillis(tweetDate: Date): Long = tweetDate.getTime

  def getNytInMillis(nytDate: String): Long = {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.ENGLISH)
    format.parse(nytDate).getTime - 21600000// subtract from GMT to local
  }

  def getNowInMillis: Long = new Date().getTime


  def getNytToday: String = {
    val simpleDateFormat = new SimpleDateFormat(NytQueryDateFormat)
    val calendar: Calendar = Calendar.getInstance()
    simpleDateFormat.format(calendar.getTime)
  }

  def getNytYesterday: String = {
    val simpleDateFormat = new SimpleDateFormat(NytQueryDateFormat)
    val calendar: Calendar = Calendar.getInstance()
    calendar.setTimeInMillis(getNowInMillis - 86400000L)
    simpleDateFormat.format(calendar.getTime) // subtract a day
  }
}
