package com.flashboomlet.db

import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.PollsterDataPoint
import com.flashboomlet.data.models.Entity
import com.flashboomlet.data.models.FinalTweet
import com.flashboomlet.data.models.MetaData
import com.flashboomlet.data.models.NewYorkTimesArticle
import com.flashboomlet.data.models.TwitterSearch
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
import scala.util.Try

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

  val twitterSearchesCollection: BSONCollection = db(TwitterSearchesCollection)

  val entitiesCollection: BSONCollection = db(EntitiesCollection)

  val pollsterDataPointsCollection: BSONCollection = db(PollsterDataPointsCollection)

  /**
    * Populates all of the entities in the database.
    *
    * If the entity already exists, differences are checked. If differences exist, the entity is
    * updated.
    *
    * @param entities Entities to insert into the databse
    */
  def populateEntities(entities: Set[Entity]): Unit = {
    Try {
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
            insert(entity, entitiesCollection)
          }
        }
      }
    }.getOrElse(
      logger.error("Failed to populate entities.")
    )
  }

  /**
    * Populate Tweet populates the tweets
    *
    * @param tweet a tweet to be inserted into the database
    */
  def insertTweet(tweet: FinalTweet): Unit = {
    insert(tweet, tweetsCollection)
  }

  /**
    * Populate Chart populates the chart collection with all new data
    *
    * @param chart a chart set to be inserted into the database
    */
  def populateChart(chart: List[PollsterDataPoint]): Unit = {
    chart.foreach { point =>
      if (!pollsterDataPointExists(point.date)) {
        insert(point, pollsterDataPointsCollection)
      }
    }
  }

  /**
    * Populate NYT Article Inserts an Article into the Article Collection
    *
    * @param article a NYT article to be inserted into the NYT article database
    */
  def insertNYTArticle(article: NewYorkTimesArticle): Unit = {
    insert(article, newYorkTimesArticlesCollection)
  }

  /**
    * Adds a metadata to a nyt article
    *
    * @param url url indicating unqiueness of article
    * @param metaData metadata to add to unique article
    */
  def addNytMetaData(url: String, metaData: MetaData): Unit = {
    val selector = BSONDocument(NYTArticleConstants.UrlString -> url)

    val future: Future[Unit] = newYorkTimesArticlesCollection
      .find(selector = selector)
      .cursor[NewYorkTimesArticle]().collect[List]()
      .map(list => list.headOption).map {
        case Some(a) =>
          val modifier = BSONDocument(GlobalConstants.SetString -> BSONDocument(
            GlobalConstants.MetaDatasString -> { a.metaDatas + metaData }
          ))

          newYorkTimesArticlesCollection.update(selector, modifier)
        case None => logger.error("Nothing makes sense. We should have retrieved the article.")
      }

    Await.result(future, Duration.Inf)
  }

  /**
    * Updates an entity's search strings.
    *
    * @param entity Entity to update search strings for in the database
    * @return update result
    */
  def updateEntity(entity: Entity): Future[UpdateWriteResult] = {
    val selector = BSONDocument(EntityConstants.LastNameString -> entity.lastName)
    val modifier = BSONDocument(GlobalConstants.SetString -> BSONDocument(
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
    val future = newYorkTimesArticlesCollection
      .find(BSONDocument(NYTArticleConstants.UrlString -> url))
      .cursor[BSONDocument]().collect[List]()
      .map(list => list.nonEmpty)

    Await.result(future, Duration.Inf)
  }

  /**
    * Determines if a NYT article is a duplicate (i.e it has the same url as one in the DB, which
    * was also found with the same search term for a given entity.
    *
    * @param url unique url related to article
    * @param query search term that retrieved article
    * @param entityLastName entity being searched
    * @return true if all 3 critery match in DB else false
    */
  def isNytDuplicate(url: String, query: String, entityLastName: String): Boolean = {
    val future = newYorkTimesArticlesCollection
      .find(BSONDocument(
        NYTArticleConstants.UrlString -> url,
        GlobalConstants.MetaDatasString ->
          BSONDocument(GlobalConstants.ElemMatchString ->
            BSONDocument(
              MetaDataConstants.EntityLastNameString -> entityLastName,
              MetaDataConstants.SearchTermString -> query
            )
          )
        )
      )
      .cursor[NewYorkTimesArticle]().collect[List]()
      .map(list => list.nonEmpty)

    Await.result(future, Duration.Inf)
  }

  /**
    * Determines if a data point exists in the database.
    *
    * @param date The data to be assessed for uniqueness in the database
    * @return true if the date exists in an pollster in the database, else false
    */
  def pollsterDataPointExists(date: String): Boolean = {
    val future =  pollsterDataPointsCollection
      .find(BSONDocument(PollsterDataPointConstants.DateString -> date))
      .cursor[BSONDocument]().collect[List]()
      .map(list => list.nonEmpty)

    Await.result(future, Duration.Inf)
  }

  /**
    * Gets a twitter search recent tweet id from a given query and entity last name
    *
    * @param query query to search for relevent TwitterSearch in DB
    * @param entityLastName Entity last name to search for relevent TwitterSearch in DB
    * @return Recent tweet id associated with the TwitterSearch if it exists, else none
    */
  def getTwitterSearch(query: String, entityLastName: String): Option[TwitterSearch] ={
    val future: Future[Option[TwitterSearch]] = twitterSearchesCollection
      .find(BSONDocument(
        TwitterSearchConstants.QueryString -> query,
        TwitterSearchConstants.EntityLastNameString -> entityLastName))
      .cursor[TwitterSearch]().collect[List]()
      .map { list => list.headOption }

    Await.result(future, Duration.Inf)
  }

  /** Simply inserts a twtitter search Model */
  def insertTwitterSearch(twitterSearch: TwitterSearch): Unit = {
    insert(twitterSearch, twitterSearchesCollection)
  }

  /**
    * Updates a twitter search model
    *
    * @param twitterSearch twitter search model to update
    */
  def updateTwitterSearch(twitterSearch: TwitterSearch): Unit = {
    val selector = BSONDocument(
      TwitterSearchConstants.QueryString -> twitterSearch.query,
      TwitterSearchConstants.EntityLastNameString -> twitterSearch.entityLastName)
    val modifier = BSONDocument(GlobalConstants.SetString -> BSONDocument(
      TwitterSearchConstants.RecentTwitterIdString -> twitterSearch.recentTwitterId
    ))
    twitterSearchesCollection.update(selector, modifier)
  }

  /**
    * Determines if a tweet is a duplicate (i.e has some tweet id, and was found via the same
    * search term for a given entity.
    *
    * @param query search term that found this tweet
    * @param entityLastName entity being searched
    * @param tweetId tweet id identifying the tweet in question
    * @return true if tweet exists matching all 3 data
    */
  def isTweetDuplicate(query: String, entityLastName: String, tweetId: Long): Boolean = {
    val future = tweetsCollection.find(BSONDocument(
      TwitterConstants.TweetIDString -> tweetId,
      GlobalConstants.MetaDatasString ->
        BSONDocument(GlobalConstants.ElemMatchString ->
          BSONDocument(
            MetaDataConstants.EntityLastNameString -> entityLastName,
            MetaDataConstants.SearchTermString -> query
          )
        )
      )
    )
    .cursor[FinalTweet]()
    .collect[List]()
    .map(list => list.nonEmpty)

    Await.result(future, Duration.Inf)
  }

  /**
    * Determines if we have already stored a tweet with a tweet id
    *
    * @param tweetId tweet id to determine if exists in db
    * @return true if tween found with tweet id else false
    */
  def tweetExists(tweetId: Long): Boolean = {
    val future = tweetsCollection.find(BSONDocument(TwitterConstants.TweetIDString -> tweetId))
      .cursor[FinalTweet]()
      .collect[List]()
      .map(list => list.nonEmpty)

    Await.result(future, Duration.Inf)
  }

  /**
    * Adds a metadata object to the sequence of metadatas associated with the tweet id
    *
    * @param tweeId tweet id identifying tweet to add metadata to
    * @param metaData metadata to add
    */
  def addTweetMetaData(tweeId: Long, metaData: MetaData): Unit = {
    val selector = BSONDocument(TwitterConstants.TweetIDString -> tweeId)

    val future: Future[Unit] = tweetsCollection
      .find(selector = selector)
      .cursor[FinalTweet]().collect[List]()
      .map(list => list.headOption).map {
        case Some(a) =>
          val modifier = BSONDocument(GlobalConstants.SetString -> BSONDocument(
            GlobalConstants.MetaDatasString -> { a.metaDatas + metaData }
          ))
        tweetsCollection.update(selector, modifier)
      case None => logger.error("Nothing makes sense. We should have retrieved the article.")
    }

    Await.result(future, Duration.Inf)
  }

  /**
    * Retrieves the BSONObjectID of an entity that exists in the database
    *
    * @note the entity MUST BE IN THE DB
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
  private def insert[T](document: T, coll: BSONCollection)(implicit writer: Writer[T]): Unit = {

    coll.insert(document).onComplete {
      case Failure(e) =>
        logger.error(s"Failed to insert and create new id into $coll")
        throw e// we fucked up
      case Success(writeResult) => // logging needed we won
    }
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
