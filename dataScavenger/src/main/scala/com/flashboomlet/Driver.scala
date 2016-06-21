package com.flashboomlet

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.flashboomlet.actors.NewYorkTimesTickActor
import com.flashboomlet.data.EntityFactory
import com.flashboomlet.data.models.Entity
import com.flashboomlet.db.MongoDatabaseDriver
import akka.actor.ActorSystem
import akka.actor.Props
import com.flashboomlet.actors.AkkaConstants
import com.flashboomlet.actors.PollsterTickActor
import com.flashboomlet.actors.TwitterTickActor
import com.flashboomlet.preproccessing.DateUtil
import com.flashboomlet.scavenger.articles.nyt.NewYorkTimesScavenger
import com.flashboomlet.scavenger.polls.PollsterScavenger
import com.flashboomlet.scavenger.twitter.TweetScavenger
import com.typesafe.scalalogging.LazyLogging

import scala.util.Try


/** Entry point to the data scavenger program */
object Driver extends LazyLogging {

  val system = ActorSystem("scavengersystem")

  /** Defines single global instance of JSON object mapper that can be used throughout. */
  implicit val objectMapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
      .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)

  /** Database driver to be used globally throughout. */
  implicit val databaseDriver: MongoDatabaseDriver = MongoDatabaseDriver()

  /** Set of globally instantiated entities */
  val entities: Set[Entity] = EntityFactory.loadEntities()

  /** Global static instance of the NYT scavenger */
  val newYorkTimesScavenger = NewYorkTimesScavenger()

  /** Global static instance of the Pollster scavenger */
  val pollsterScavenger = PollsterScavenger()

  /** Global static instance of the Tweet scavenger */
  val tweetScavenger = TweetScavenger()

  /** Main entry point to the program */
  def main(args: Array[String]): Unit = {

    databaseDriver.populateEntities(entities)

    configureScheduler()
  }

  /**
    * Configures and schedules all of the tick actors.
    */
  def configureScheduler(): Unit = {
    Try {
      import system.dispatcher // scalastyle:ignore import.grouping

      val nytActor = system.actorOf(Props(classOf[NewYorkTimesTickActor]))
      val twitterActor = system.actorOf(Props(classOf[TwitterTickActor]))
      val pollsterActor = system.actorOf(Props(classOf[PollsterTickActor]))

      system.scheduler.schedule(
        AkkaConstants.InitialDelay,
        AkkaConstants.TwitterTickLength,
        twitterActor,
        AkkaConstants.Tick)

      system.scheduler.schedule(
        AkkaConstants.InitialDelay,
        AkkaConstants.NewYorkTimesLength,
        nytActor,
        AkkaConstants.Tick)

      system.scheduler.schedule(
        AkkaConstants.InitialDelay,
        AkkaConstants.PollsterLength,
        pollsterActor,
        AkkaConstants.Tick)
    }.getOrElse(logger.error("Failed to configure scheduler."))
  }
}
