package com.flashboomlet.db.implicits

import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.Entity
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONString

/** Implicit readers and writers for the entity model in MongoDB */
trait EntityImplicits extends MongoConstants {

  /** Implicit writer for Entity class */
  implicit object EntityWriter extends BSONDocumentWriter[Entity] {

    override def write(entity: Entity): BSONDocument = BSONDocument(
      EntityConstants.NameString -> BSONString(entity.choice)
    )
  }

  /** Implicit reader for Entity class */
  implicit object EntityReader extends BSONDocumentReader[Entity] {

    override def read(doc: BSONDocument): Entity = {
      val entityName = doc.getAs[String](EntityConstants.NameString).get

      Entity(entityName)
    }
  }
}
