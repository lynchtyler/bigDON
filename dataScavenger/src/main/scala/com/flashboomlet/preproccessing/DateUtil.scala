package com.flashboomlet.preproccessing

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

import org.json4s.DefaultFormats
import com.github.nscala_time.time.Imports._

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

  /**
    * getToday returns today's date time in iso format
    *
    * @return iso formatted dateTime
    */
  def getToday(): String = {
    Instant.now().toString
  }

  /**
    * convertTwitterDate converts a twitters date to the proper ISO format on the UTC time standard
    *
    * @param date twitter date
    * @return iso formatted dateTime in a String
    */
  def convertTwitterDate(date: Date): String = {
    date.toString
  }

  /**
    * Takes a short date and converts it to a full ISO format.
    *
    * @param date date
    * @return iso formatted dateTime in a String
    */
  def shortDateNormalize(date: Date): String = {
    date.toString
  }

  /**
    * normalizeDate takes a string and normalizes the date to UTC time
    *
    * @param date a date to be normalized
    * @return a normalized date
    */
  def normalizeDate(date: String): String = {
    DateTime.parse(date).toString
  }

  /**
    * Formatter for dates, specifically used for JSON formatting
    */
  private val defaultFormats = new DefaultFormats {
    override def dateFormatter = {
      val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ")
      simpleDateFormat
    }
  }
}
