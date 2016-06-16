package com.flashboomlet.scavenger

import com.flashboomlet.data.models.Entity

/**
  * Created by ttlynch on 6/15/16.
  */
trait Scavenger {

  def scavenge(entity: Entity): Unit

}
