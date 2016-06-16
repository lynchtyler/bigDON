package com.flashboomlet.data

import java.io.File

import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.data.models.Entity

/**
  * Created by trill on 6/16/16.
  */
object EntityFactory {

  def loadEntities()(implicit mapper: ObjectMapper): Set[Entity] = {
    val resource = ClassLoader.getSystemClassLoader.getResource("entities.json")

    val file = new File(resource.toURI)

    mapper.readValue(file, classOf[Entities]).entities
  }

  private case class Entities(entities: Set[Entity])
}
