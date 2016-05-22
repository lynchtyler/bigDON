package data

import java.io.File

/**
  * Object containing type definitions used for data source handling.
  */
object sourceTypes {

  /** Type used for bounding source files applicable to wordStorm */
  type Source = File

  /** Type for a JSON source file */
  type JSON = Source

  /** Type for an XML source file */
  type XML = Source

  /** Type for an HTML source file */
  type HTML = Source

  /** Type for a Text source file */
  type TXT = Source

}
