package com.flashboomlet.data.twitter

import com.flashboomlet.data.DataSource
import com.flashboomlet.data.sourceTypes.JSON
import com.flashboomlet.data.sourceTypes.Source

/**
  * Class representing a Twitter [[JSON]] [[DataSource]]. Used in source modification of the twitter
  * and allows access to the linguistic twitter data contained within the Twitter [[Source]]
  * file.
  *
  * @param sourceFile The [[JSON]] [[Source]] file containing the Twitter JSON data
  */
class TwitterJSONDataSource(val sourceFile: Source) extends DataSource[JSON] {

  /**
    * Gets the data stream of the linguistic from the [[Source]] that is a candidate for
    * linguistic analysis.
    *
    * @note Currently returns Unit until an appropriate linguistic data model is decided upon.
    * @return Unit
    */
  override def getLinguisticData(): Unit = ()
}

/**
  * Companion object for the [[TwitterJSONDataSource]] class.
  */
object TwitterJSONDataSource {

  /**
    * Default factory method for constructing a [[TwitterJSONDataSource]]
    *
    * @param source The [[Source]] to wrapped by the [[TwitterJSONDataSource]]
    * @return A new instance of a [[TwitterJSONDataSource]]
    */
  def apply(source: Source): TwitterJSONDataSource = {
    new TwitterJSONDataSource(source)
  }
}
