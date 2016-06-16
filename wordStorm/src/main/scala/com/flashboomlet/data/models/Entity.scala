package com.flashboomlet.data.models

/**
  * Case class for the entity
  *
  * @param name the choice tag line ("Last Name", Other)
  */
case class Entity(
 name: String
)

/**
  * Case class for the Estimates data
  *
  * @param choice the choice tag line ("Last Name", Other)
  * @param value current value
  * @param lead_confidence the confidence that the choice is leading
  * @param first_name the choice first name ("Name", null)
  * @param last_name the choice last name ("Name", null)
  * @param party the party of the choice (Dem, Rep, N/A)
  * @param searchTerms terms to search by
  */
case class EntityMetaData(
  choice: String,
  value: Float,
  lead_confidence: Float,
  first_name: String,
  last_name: String,
  party: String,
  searchTerms: Option[Seq[String]] = None
)