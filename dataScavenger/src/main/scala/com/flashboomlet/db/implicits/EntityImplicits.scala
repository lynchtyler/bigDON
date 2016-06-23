package com.flashboomlet.db.implicits

import com.flashboomlet.data.models.Entity
import com.flashboomlet.db.MongoConstants
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONString

/** Implicit readers and writers for the entity model in MongoDB */
trait EntityImplicits extends MongoConstants {

  /** Implicit writer for Entity class */
  implicit object EntityWriter extends BSONDocumentWriter[Entity] {

    override def write(entity: Entity): BSONDocument = BSONDocument(
      EntityConstants.FirstNameString -> BSONString(entity.firstName),
      EntityConstants.LastNameString -> BSONString(entity.lastName),
      EntityConstants.PartyString -> BSONString(entity.party),
      EntityConstants.SearchTermsString -> entity.searchTerms
    )
  }

  /** Implicit reader for Entity class */
  implicit object EntityReader extends BSONDocumentReader[Entity] {

    override def read(doc: BSONDocument): Entity = {
      val firstName = doc.getAs[String](EntityConstants.FirstNameString).get
      val lastName = doc.getAs[String](EntityConstants.LastNameString).get
      val party = doc.getAs[String](EntityConstants.PartyString).get
      val searchTerms = doc.getAs[Set[String]](EntityConstants.SearchTermsString).getOrElse(Set())

      Entity(
        firstName = firstName,
        lastName = lastName,
        party = party,
        searchTerms = searchTerms
      )
    }
  }
}
