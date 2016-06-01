package data.modification

import com.flashboomlet.data.DataSource
import com.flashboomlet.data.modification.DataSourceTransmuter
import com.flashboomlet.data.sourceTypes.Source

import scalaz.\/

/**
  * Trait defining the behavior of a transmuter that only sanitizes a [[Source]] file. The
  * sanitation process does not change the type of the [[DataSource]], it only changes the
  * content of it. This trait is used for removing extraneous fields from a [[DataSource]]
  * to be used.
  *
  * @tparam T The [[Source]] type of the [[DataSource]] to be sanitized.
  */
trait DataSourceSanitizationTransmuter[T <: Source] extends DataSourceTransmuter[T, T] {

  /**
    * Sanitizes a [[DataSource]] by removing any unwanted data, restructuring the data, or modifying
    * the data. The sanitization process returns a [[DataSource]] of the same type, so if a
    * sanitization process calls for producing a new type of [[DataSource]], consider using
    * another [[DataSourceTransmuter]].
    *
    * @param dirtySource The [[DataSource]] to be sanitized
    * @return The sanitized [[DataSource]]
    */
  def sanitize(dirtySource: DataSource[T]): \/[Error, DataSource[T]]

  /**
    * Transmutations of a sanitizing module should simply return the sanitized result.
    *
    * @param dataSource The [[DataSource]] to be transmuted
    * @return The [[DataSource]] resulting from the transmutation
    */
  override def transmute(dataSource: DataSource[T]): \/[Error, DataSource[T]] = sanitize(dataSource)
}
