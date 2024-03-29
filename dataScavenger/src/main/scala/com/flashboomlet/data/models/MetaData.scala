package com.flashboomlet.data.models

/**
  * Case class for the metadata data model
  *
  * @param fetchDate the date that the data was pulled
  * @param publishDate the date that the data was published
  * @param source the source of the data
  * @param searchTerm the search term used in fetching the data
  * @param entityLastName the ID of the entity
  * @param contributions the contributions; Ex: Count of Authors, Count of Polls, etc...
  */
case class MetaData(
  fetchDate: Long,
  publishDate: Long,
  source: String,
  searchTerm: String,
  entityLastName: String,
  contributions: Int
)
