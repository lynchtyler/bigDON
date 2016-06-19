package com.flashboomlet.actors

import akka.actor.Actor
import com.flashboomlet.Driver

/**
  * Class used for scheduling functions on a tick cycle
  */
class NewYorkTimesTickActor extends Actor {

  /** What to do on receiving a tick message */
  def receive: Actor.Receive = {
    case AkkaConstants.Tick => Driver.newYorkTimesScavenger.scavenge()
  }
}
