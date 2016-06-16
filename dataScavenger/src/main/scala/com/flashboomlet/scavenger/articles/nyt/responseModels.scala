package com.flashboomlet.scavenger.articles.nyt

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
  * Case class for top level SearchMetaData.
  *
  * @param status The response status (as an english word)
  * @param response The actual metadata response
  */
@JsonIgnoreProperties(Array("copyright"))
case class SearchMetaData(status: String, response: MetaDatas)

/**
  * Case class for the meta data
  *
  * @param meta the wrapper
  */
@JsonIgnoreProperties(Array("docs"))
case class MetaDatas(meta: Meta)

/**
  * Case class for the meta data
  *
  * @param hits number of articles the query returned
  * @param time the time the query took
  * @param offset the starting article (affected by page number)
  */
case class Meta(hits: Int, time: Int, offset: Int)

/**
  * Case class for article response model. Excludes meta data
  *
  * @param response Wraps the response
  * @param copyright #famous
  */
@JsonIgnoreProperties(Array("status"))
case class ArticleResponse(response: Response, copyright: String)

/**
  * Case class wrapping the articles
  *
  * @param docs The sequence of article "docs"
  */
@JsonIgnoreProperties(Array("meta"))
case class Response(docs: Set[Doc])

/**
  * Case class of the important stuff
  *
  * @param web_url url location of the article
  * @param snippet snippet of the article
  * @param lead_paragraph first paragraph of the article
  * @param source Where the article is from
  * @param headline The title of the article
  * @param section_name The topic/subject of the article
  * @param subsection_name A more specific topic/subject for the article
  * @param keywords Some weird stuff. Still not used.
  * @param pub_date Article publish date
  * @param byline Author and contributors
  * @param word_count number of words in the article
  */
@JsonIgnoreProperties(Array(
  "abstract", "blog", "print_page", "multimedia", "news_desk", "type_of_material", "_id",
  "slideshow_credits"))
case class Doc(
  web_url: String,
  snippet: String,
  lead_paragraph: String,
  source: String,
  headline: Headline,
  section_name: String,
  subsection_name: String,
  keywords: Set[Keyword],
  pub_date: String,
  byline: ByLine,
  document_type: String,
  word_count: Int
)

/**
  * Case class for title of article
  *
  * @param main Main title
  * @param print_headline Alternate printed title
  */
@JsonIgnoreProperties(Array("kicker", "content_kicker", "seo"))
case class Headline(main: String, print_headline: String)

/**
  * Case class for the keyword models
  * @param rank importance
  * @param is_major is it a major key word or not? don't waste my time...
  * @param name the key for the word
  * @param value the word
  */
case class Keyword(rank: Int, is_major: String, name: String, value: String)

/**
  * Case class containing author and contributor information.
  *
  * @param contributor String listing all contributor data
  * @param person An author or list of authors
  */
@JsonIgnoreProperties(Array("original", "organization"))
case class ByLine(contributor: String, person: Set[Person])

/**
  * Case class for a contributing author
  *
  * @param firstname The firstname of the author
  * @param middlename the middlename of the author
  * @param lastname the lastname of the author
  */
@JsonIgnoreProperties(Array("rank", "role", "organization"))
case class Person(firstname: String, middlename: String, lastname: String)

