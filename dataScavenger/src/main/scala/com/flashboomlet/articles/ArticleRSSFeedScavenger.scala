package com.flashboomlet.articles

import com.flashboomlet.data.DataSource
import com.flashboomlet.data.rss.RSSDataSource
import com.flashboomlet.data.sourceTypes.RSS

/**
  * Trait defining behavior of an article fetcher for a journalism website, online newspaper,
  * or other general new outlet that primarily makes their article available through an RSS,
  * or rich site summary, file.
  */
trait ArticleRSSFeedScavenger {

  /** The URL of the constantly updated RSS file to fetch */
  val StaticRSSLinkLocation: String

  /**
    * Fetches the RSS file for an RSS-based article scavenger
    *
    * The RSS file contains a list of links and metadata of articles that may be fetched.
    *
    * @return an [[RSS]] [[DataSource]]
    */
  def fetchRSSFile(): RSSDataSource
}
