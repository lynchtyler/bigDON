package com.flashboomlet.data.modification

import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.data.DataSource
import com.flashboomlet.data.sourceTypes.JSON
import data.modification.DataSourceSanitizationTransmuter

import scalaz.\/

/**
  * Trait to be used in all JSON sanitization modules. This trait (At the moment) requires an
  * implicit [[ObjectMapper]] to be present. There should only be one [[ObjectMapper]] instantiated
  * in the program at a time due to overhead.
  *
  * The [[ObjectMapper]] will then sanitize the original [[DataSource]] based on the sanitize
  * method definition. The sanitize method definition should use the object mapper and destiation
  * schema to produce the sanitized [[DataSource]].
  *
  * This class is a work in progress. Will be reviewed upon first implementation of this trait.
  */
trait JSONSanitizationTransmuter extends DataSourceSanitizationTransmuter[JSON] {

  /**
    * Implicit val used for reading/writing JSON files based on the schema provided in an Object
    * of type [[AnyRef]].
    */
  implicit val objectMapper: ObjectMapper

  /**
    * Destination Schema, or the schema of the sanitized [[DataSource]]
    */
  val destinationSchema: AnyRef

  /** @inheritdoc */
  override def sanitize(dirtySource: DataSource[JSON]): \/[Error, DataSource[JSON]]
}
