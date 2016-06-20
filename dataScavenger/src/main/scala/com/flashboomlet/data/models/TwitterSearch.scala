package com.flashboomlet.data.models

/**
  * Case class for the Twitter Search model. Used to prevent suploicate fetching.
  *
  * @param query Query of the search
  * @param entityLastName Entity of the search
  * @param recentTwitterId The recent twitter ID associated with this query/entity
  */
case class TwitterSearch(
  query: String,
  entityLastName: String,
  recentTwitterId: Long
)
