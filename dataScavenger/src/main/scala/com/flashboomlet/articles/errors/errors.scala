package com.flashboomlet.articles.errors

/**
  * An error that occurs during searching
  *
  * @param message Additional information about the nature of the error.
  */
case class SearchError(message: String) extends Error
