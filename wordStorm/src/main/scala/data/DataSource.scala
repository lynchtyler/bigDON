package data

import data.sourceTypes.Source

/**
  * Trait defining a Data source that may be in interacted with, transformed, or sanitized.
  * Some example data sources include XML files, JSON files, and Text files.
  *
  * Classes extending this trait should define getLinguisticData in such a way that for this
  * particular [[DataSource]], the core text in which sentiment analysis will be tested against
  * will be returned. This model core text used for analysis is currently undecided upon.
  *
  * @tparam T The type of the source file, bounded by the [[Source]], which is a [[java.io.File]]
  */
trait DataSource[T <: Source] {

  /** This [[Source]] being wrapped. */
  val sourceFile: Source

  /**
    * Gets the text inside of the [[Source]] file that is to be used in linguistic analyses.
    *
    * @return Currently returns unit until a model for linguistic data is created.
    */
  def getLinguisticData: Unit
}
