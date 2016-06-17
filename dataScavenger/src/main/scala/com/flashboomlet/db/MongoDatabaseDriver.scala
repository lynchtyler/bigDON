package com.flashboomlet.db

import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.Entity
import com.flashboomlet.db.implicits.MongoImplicits
import com.typesafe.scalalogging.LazyLogging
import reactivemongo.api.BSONSerializationPack.Writer
import reactivemongo.api.MongoDriver
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.UpdateWriteResult
import reactivemongo.bson.BSONArray
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
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
    * Populates all of the entities in the database.
    *
    * If the entity already exists, differences are checked. If differences exist, the entity is
    * updated.
    *
    * @param entities Entities to insert into the databse
    */
  def populateEntities(entities: Set[Entity]): Unit = {
    entities.foreach { (entity: Entity) =>
      entitiesCollection.find(
        BSONDocument(EntityConstants.LastNameString -> entity.lastName)
      ).cursor[Entity]().collect[List]().map { list =>
        if (list.nonEmpty) {
          if (!list.head.equals(entity)) {
            logger.info("Updating entity: " + entity.lastName)
            updateEntity(entity)
          }
        } else {
          logger.info("Creating entity: " + entity.lastName)
          insertAndRetrieveNewId(entity, entitiesCollection)
        }
      }
    }
  }

  /**
    * Updates an entity's search strings.
    *
    * @param entity Entity to update search strings for in the database
    * @return update result
    */
  def updateEntity(entity: Entity): Future[UpdateWriteResult] = {
    val selector = BSONDocument(EntityConstants.LastNameString -> entity.lastName)
    val modifier = BSONDocument("$set" -> BSONDocument(
      EntityConstants.SearchTermsString -> entity.searchTerms
    ))
    entitiesCollection.update(selector, modifier)
  }

  /**
    * Determines if a new york times article with the provided URL exists in the database.
    *
    * @param url The URL to be assessed for uniqueness in the database
    * @return true if the url exists in an article in the database, else false
    */
  def newYorkTimesArticleExists(url: String): Boolean = {
    val future =  newYorkTimesArticlesCollection
      .find(BSONDocument(NYTArticleConstants.UrlString -> url))
      .cursor[BSONDocument]().collect[List]()
      .map(list => if (list.nonEmpty) { true } else { false })

    Await.result(future, Duration.Inf)
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
