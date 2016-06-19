package com.flashboomlet.scavenger

import com.flashboomlet.Driver
import com.flashboomlet.data.models.Entity

/**
  * Created by ttlynch on 6/15/16.
  */
trait Scavenger {

  /**
    * Scavenges data for the entities
    * @param entities Entities to scavenge
    */
  def scavenge(entities: Set[Entity] = Driver.entities): Unit

}
