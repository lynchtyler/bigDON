package com.flashboomlet.actors

import akka.actor.Actor
import com.flashboomlet.Driver

/**
  * Class used for scheduling functions on a tick cycle
  */
class PollsterTickActor extends Actor {

  /** What to do on receiving a tick message */
  def receive: Actor.Receive = {
    case AkkaConstants.Tick => Driver.pollsterScavenger.scavenge()
  }
}
