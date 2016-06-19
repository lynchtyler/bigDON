package com.flashboomlet.data.models

/**
  * Case class for the entity
  *
  * @param firstName the choice first name ("Name", null)
  * @param lastName the choice last name ("Name", null)
  * @param party the party of the choice (Dem, Rep, N/A)
  * @param searchTerms terms to search by
  */
case class Entity(
  firstName: String,
  lastName: String,
  party: String,
  searchTerms: Set[String]
)
