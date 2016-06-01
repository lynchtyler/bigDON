package com.flashboomlet.data.rss

import com.flashboomlet.data.DataSource
import com.flashboomlet.data.PrefetchArticle
import com.flashboomlet.data.sourceTypes.RSS

/**
  * Trait defining the behavior of an [[RSS]] [[DataSource]]
  */
trait RSSDataSource extends DataSource[RSS] {

  /**
    * Gets a [[Vector]] of prefetch articles from an RSS file.
    *
    * The [[PrefetchArticle]] may later be used to actually access and process the article.
    *
    * @return A [[Vector]] of [[PrefetchArticle]]
    */
  def getPrefetchArticles: Vector[PrefetchArticle]
}
