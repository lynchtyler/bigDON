package com.flashboomlet.data.models

/**
  * Case class for the counts data model.
  *
  * @param wordCount the count of words within the main content
  * @param sentenceCount the count of sentences within the main content
  * @param titleWordCount the count of words within the title
  * @param searchTermCount the count of search terms
  */
case class Counts(
  wordCount: Int,
  sentenceCount: Int,
  titleWordCount: Int,
  searchTermCount: Int,
  wordOccurrences: Map[String, Int]
)
