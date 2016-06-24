package com.flashboomlet.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.data.models.Entity

/**
  * Created by trill on 6/16/16.
  */
object EntityFactory {

  def loadEntities()(implicit mapper: ObjectMapper): Set[Entity] = {
    val resource = EntityFactory.getClass.getClassLoader.getResourceAsStream("entities.json")


    mapper.readValue(resource, classOf[Entities]).entities
  }

  private case class Entities(entities: Set[Entity])
}
