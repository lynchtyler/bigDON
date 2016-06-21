package com.flashboomlet.preproccessing

import com.flashboomlet.data.models.Counts
import scala.collection.immutable.ListMap

/**
  * The count utility
  */
object CountUtil {

  private val commonWords = List("","the","be","to","of","and","a","in","that","have","i","it",
  "for","not","on","with","he","as","you","do","at","this","but","his","by","from","they","we",
  "say","her","she","or","an","will","my","one","all","would","there","their","what","so","up",
  "out","if","about","who","get","which","go","me","when","make","can","like","time","no","just",
  "him","know","take","person","into","year","your","good","some","could","them","see","other",
  "than","then","now","look","only","come","its","over","think","also","back","after","use","two",
  "how","our","work","first","well","way","even","new","want","because","any","these","give","day",
  "most","us","mr","mrs","is","was","are","has","where","why")

  private val space: String = " "

  private val listAdjustmentLength = 1

  /**
    * Word Count in a string
    *
    * @param content the content
    * @return the word count
    */
  private def wordCount(content: String): Int = content.split(space).length

  /**
    * Sentence count of a string
    *
    * @param content the content
    * @return the sentence count
    */
  private def sentenceCount(content: String): Int = content.split("[.!?]+").length

  /**
    * Word Occurrences for the content
    *
    * @param content the content
    * @return the occurrences of the words
    */
  private def wordOccurrences(content: String): Seq[(String, Int)] = {
    val cleanedWords = content.split(space).map {
      _.replaceAll("[\\u2018\\u2019]","'")
      .replaceAll("'s","")
      .replaceAll("[^A-Za-z]@#","")
      .toLowerCase
    }
    cleanedWords.distinct.map{ (unique: String) =>
      (unique, cleanedWords.count(other => other.equals(unique)))
    }
  }

  /**
    * Test to see if a word is a common word
    *
    * @param word the word to check against
    * @return true if it is a common word
    */
  private def commonWord(word: String): Boolean = commonWords.contains(word)

  /**
    * The Count of the Search term words
    *
    * @param content
    * @param searchTerm
    * @return
    */
  private def searchTermCount(
    content: String,
    searchTerm: String): Int = {
    content.toLowerCase.split(searchTerm).length-listAdjustmentLength
  }

  /**
    * The Counts of the Content
    *
    * @param title the title of the content
    * @param content the content
    * @param searchTerm the search term
    * @return a count object of the counts
    */
  def countContent(
    title: String = "",
    content: String,
    searchTerm: String): Counts = {

    val occurrences = ListMap(wordOccurrences(content).sortWith(_._2 > _._2): _*)

    val ten: Int = 20
    val topTerms = ListMap(occurrences.filterKeys((s: String) =>
      !commonWord(s)).take(ten).toSeq.sortWith(_._2 > _._2): _*)

    Counts(
      wordCount = wordCount(content),
      sentenceCount = sentenceCount(content),
      titleWordCount = wordCount(title),
      searchTermCount = searchTermCount(content, searchTerm),
      wordOccurrences = topTerms
    )
  }

}
