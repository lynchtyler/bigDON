package com.flashboomlet.db.implicits

import com.flashboomlet.data.models.Counts
import com.flashboomlet.db.MongoConstants
import com.flashboomlet.db.MongoUtil
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONInteger

/** Implicit readers and writers for the Counts model in MongoDB */
trait CountsImplicits extends MongoConstants {

  /** Implicit writer for the Counts class */
  implicit object CountsWriter extends BSONDocumentWriter[Counts] {

    override def write(counts: Counts): BSONDocument = BSONDocument(
      CountsConstants.WordCountString -> BSONInteger(counts.wordCount),
      CountsConstants.SentenceCountString -> BSONInteger(counts.sentenceCount),
      CountsConstants.TitleWordCountString -> BSONInteger(counts.titleWordCount),
      CountsConstants.SearchTermCountString -> BSONInteger(counts.searchTermCount),
      CountsConstants.WordOccurrencesString -> MongoUtil.mapToBSONDocument(counts.wordOccurrences)
    )
  }

  /** Implicit reader for Counts class */
  implicit object CountsReader extends BSONDocumentReader[Counts] {

    override def read(doc: BSONDocument): Counts = {
      val wordCount = doc.getAs[Int](CountsConstants.WordCountString).get
      val sentenceCount = doc.getAs[Int](CountsConstants.SentenceCountString).get
      val titleWordCount = doc.getAs[Int](CountsConstants.TitleWordCountString).get
      val searchTermCount = doc.getAs[Int](CountsConstants.SearchTermCountString).get
      val wordOccurrences = doc.getAs[BSONDocument](CountsConstants.WordOccurrencesString).get

      Counts(
        wordCount = wordCount,
        sentenceCount = sentenceCount,
        titleWordCount = titleWordCount,
        searchTermCount = searchTermCount,
        wordOccurrences = MongoUtil.bsonDocumentToMap(
          wordOccurrences)
      )
    }
  }
}
