package com.flashboomlet.actors

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.MILLISECONDS
import scala.concurrent.duration.SECONDS
import scala.concurrent.duration.HOURS

/**
  * String constants used in akka systems
  */
object AkkaConstants {

  /** String constant for the tick message */
  val Tick = "tick"

  /** Initial delay for scheduling actors, in milliseconds */
  private[this] val InitialMilliseconds = 5000

  /** Length between schedule ticks for a twitter actor, in milliseconds */
  private[this] val TwitterSeconds = 60

  /** Length between schedule ticks for a new york times actor, in hours */
  private[this] val NewYorkTimesHours = 24

  /** Length between schedule ticks for a pollster scavenger, in hours */
  private[this] val PollsterHours = 24

  // FINITE DURATIONS //

  /** Finite duration for the initial wait time on actor scheduling */
  val InitialDelay = FiniteDuration(InitialMilliseconds, MILLISECONDS)

  /** Finite duration for time between tweet fetching */
  val TwitterTickLength = FiniteDuration(TwitterSeconds, SECONDS)

  /** Finite duration for time between article fetching */
  val NewYorkTimesLength = FiniteDuration(NewYorkTimesHours, HOURS)

  /** Finite duration for time between pollster fetching */
  val PollsterLength = FiniteDuration(PollsterHours, HOURS)
}
