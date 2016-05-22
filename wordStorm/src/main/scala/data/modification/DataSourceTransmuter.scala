package data.modification

import data.DataSource
import data.sourceTypes.Source

import scalaz.\/

/**
  * Trait defining the generic behavior of something that can transmute one data source into
  * another. This is used for converting [[DataSource]] objects into new [[DataSource]] objects.
  *
  * The transmutations are mainly used for cleaning up data or converting one type of [[Source]]
  * into a more easily usable [[Source]] for linguistic analysis.
  *
  * @tparam T The [[Source]] type of the [[DataSource]] to be transmuted.
  * @tparam R The [[Source]] type of the [[DataSource]] of the resulting transmutation.
  */
trait DataSourceTransmuter[T <: Source, R <: Source] {

  /**
    * Transmutes a [[DataSource]] of type [[T]] into a [[DataSource]] of type [[R]]. The
    * transmutation should not perform any analysis on the content of the [[Source]]. The
    * transmutation may, however, clean up the source, remove extraneous fields, or parse the fields
    * into a more easily dealt with [[Source]] format.
    *
    * @note Method must be overridden by a Transmuter extending this trait.
    *
    * @param originalSource The [[DataSource]] to be transmuted
    * @return The [[DataSource]] resulting from the transmutation
    */
  def transmute(originalSource: DataSource[T]): \/[Error, DataSource[R]]
}
