package com.flashboomlet.db

import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.Entity
import com.flashboomlet.db.implicits.MongoImplicits
import com.typesafe.scalalogging.LazyLogging
import reactivemongo.api.BSONSerializationPack.Writer
import reactivemongo.api.MongoDriver
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success

/**
  * Database driver for MongoDB
  */
class MongoDatabaseDriver
  extends MongoConstants
  with MongoImplicits
  with LazyLogging {

  val driver = new MongoDriver()

  val connection = driver.connection(List(LocalHostString))

  val db = connection(BigDonDatabaseString)

  val newYorkTimesArticlesCollection: BSONCollection = db(NewYorkTimesArticlesCollection)

  val tweetsCollection: BSONCollection = db(TweetsCollection)

  val entitiesCollection: BSONCollection = db(EntitiesCollection)

  /**
    * Populates all of the entities in the database. If their IDS are later needed, you should map
    * then entities instead of foreach.
    *
    * @param entities Entities to insert into the databse
    */
  def populateEntities(entities: Set[Entity]): Unit = {
    // create assessment in the database if it does not exist and retrieve id
    entities.foreach { (entity: Entity) =>
      entitiesCollection.find(
        BSONDocument(EntityConstants.NameString -> entity.name)
      ).cursor[BSONDocument]().collect[List]().map { list =>
        if (list.nonEmpty) {
          list.head.get(GlobalConstants.IdString).get.asInstanceOf[BSONObjectID]
        } else {
          insertAndRetrieveNewId(entity, entitiesCollection)
        }
      }
    }
  }

  /**
    * Retrieves the BSONObjectID of an entity that exists in the database
    *
    * @note the entity MUST BE IN THE DB
    *
    * @param entity entity to retrieve BSON ID for
    * @return The bson object id of the entity
    */
  def getEntityId(entity: Entity): BSONObjectID = {
    val future = entitiesCollection.find(entity)
      .cursor[BSONDocument]()
      .collect[List]()
      .map(list => list.head.get(GlobalConstants.IdString).get.asInstanceOf[BSONObjectID])

    Await.result(future, Duration.Inf)
  }

  /**
    * Inserts an object with a defined implicit BSONWriter into the provided
    * collection and then returns the new BSONObjectID associated with the
    * newly inserted document.
    *
    * @param document Class of type T to be inserted as a [[BSONDocument]]
    * @param coll Collection to insert the [[BSONDocument]] into
    * @param writer Implicit writer associated with the Object of type T
    * @tparam T Type of the object to be inserted
    * @return The  [[BSONObjectID]] associated with the newly created document
    */
  private def insertAndRetrieveNewId[T](
      document: T,
      coll: BSONCollection,
      id: Option[String] = None)
      (implicit writer: Writer[T]): BSONObjectID = {

    val newId: BSONObjectID = id match {
      case Some(s) => BSONObjectID(s)
      case None => BSONObjectID.generate()
    }

    coll.insert(writer.write(document).add(GlobalConstants.IdString -> id)).onComplete {
      case Failure(e) => throw e // we fucked up
      case Success(writeResult) => // logging needed we won
    }
    newId
  }
}

/**
  * Companion object for the MongoDatabaseDriver class
  */
object MongoDatabaseDriver {

  /**
    * Factory constructor method for the mongoDB database driver
 *
    * @return a new instance of MongoDatabseDriver
    */
  def apply(): MongoDatabaseDriver = new MongoDatabaseDriver
}
