package com.flashboomlet

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.flashboomlet.data.EntityFactory
import com.flashboomlet.data.models.Entity
import com.flashboomlet.db.MongoDatabaseDriver
import com.flashboomlet.scavenger.Scavenger
import com.flashboomlet.scavenger.articles.nyt.NewYorkTimesScavenger
import com.flashboomlet.scavenger.polls.PollsterScavenger
import com.flashboomlet.scavenger.twitter.TweetScavenger

/** Entry point to the data scavenger program */
object Driver {
  /** Defines single global instance of JSON object mapper that can be used throughout. */
  implicit val objectMapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
      .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)

  /** Database driver to be used globally throughout. */
  implicit val databaseDriver: MongoDatabaseDriver = MongoDatabaseDriver()

  /** Set of globally instantiated entities */
  val entities: Set[Entity] = EntityFactory.loadEntities()

  /** Main entry point to the program */
  def main(args: Array[String]): Unit = {

    databaseDriver.populateEntities(entities)

    val scavengers: Seq[Scavenger] = Seq(
      NewYorkTimesScavenger(),
      TweetScavenger(),
      PollsterScavenger()
    )
  }
}
