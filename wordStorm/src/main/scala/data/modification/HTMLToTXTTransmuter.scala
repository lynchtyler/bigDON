package data.modification

import data.sourceTypes.HTML
import data.sourceTypes.TXT
import data.DataSource

/**
  * Trait used for transmuting an [[HTML]] [[DataSource]] into a [[TXT]] [[DataSource]].
  *
  * The specific rules for denining how the important data should be extracted/parsed from the
  * [[HTML]] source will be defined by what extends this trait. More generic requirements for what
  * is needed to perform this transmutation will be definited when an HTML parsing library is
  * decided on.
  */
trait HTMLToTXTTransmuter extends DataSourceTransmuter[HTML, TXT]
