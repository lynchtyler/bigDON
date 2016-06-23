package com.flashboomlet.data

/**
  * Case class for a prefetched article for articles being fetched via an RSS feed.
  *
  * Many of the parameters defining a prefetched article are unknown as of right now.
  *
  * @param url The url location of the Article
  * @param title The title of the article
  * @param subjects The topics/subjects that the article is contrained to
  * @param summaries The snippets, first paragraph, summaries, etc.. of the article
  * @param publishDate The date the article was published
  * @param fetchDate The date the article was retrieved
  * @param source The source/publisher of the article
  * @param author The author of the article
  * @param keyPeople A list of key names (contributors, interviewees, etc..) associated with the
  *                  article
  * @param keyWords Key words associated with the article. This data structure will be implemented
  *                 at a later time
  * @param wordCount The optional word count of the article
  */
case class PrefetchArticle(
  url: String,
  title: String,
  subjects: Set[String] = Set(),
  summaries: Set[String] = Set(),
  publishDate: Long,
  fetchDate: Long,
  source: String,
  author: String,
  keyPeople: Set[String] = Set(),
  keyWords: Set[String] = Set(),
  wordCount: Option[Int] = None
)
