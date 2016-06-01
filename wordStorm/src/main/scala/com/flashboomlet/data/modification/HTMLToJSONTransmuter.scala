package com.flashboomlet.data.modification

import com.flashboomlet.data.sourceTypes.HTML
import com.flashboomlet.data.DataSource
import com.flashboomlet.data.sourceTypes.JSON

/**
  * Trait used for transmuting an [[HTML]] [[DataSource]] into a [[JSON]] [[DataSource]].
  *
  * The specific rules for defining how the important data should be extracted/parsed from the
  * [[HTML]] source will be defined by what extends this trait. More generic requirements for what
  * is needed to perform this transmutation will be defined when an HTML parsing library is
  * decided on.
  */
trait HTMLToJSONTransmuter extends DataSourceTransmuter[HTML, JSON]
