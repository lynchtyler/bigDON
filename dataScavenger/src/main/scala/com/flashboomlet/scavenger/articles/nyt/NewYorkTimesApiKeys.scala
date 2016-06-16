package com.flashboomlet.scavenger.articles.nyt

/**
  * Case class for wrapping all of the different New York Times api keys
  *
  * @param articleSearch Api key for the articlesearch endpoint
  * @param mostPopular Currently not being used
  */
case class NewYorkTimesApiKeys(articleSearch: String, mostPopular: String)
