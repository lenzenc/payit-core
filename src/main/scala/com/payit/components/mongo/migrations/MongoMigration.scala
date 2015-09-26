package com.payit.components.mongo.migrations

import com.mongodb.casbah.{MongoDB, MongoCollection}
import com.mongodb.casbah.commons.MongoDBObject

abstract class MongoMigration(db: MongoDB) {

  def collectionName: String
  protected lazy val collection = db(collectionName)

  def up: Unit

  def down: Unit

  def addUniqueIndex(name: String, fields: String*) = {
    var map = MongoDBObject()
    fields.foreach { i => map.put(i, 1) }
    collection.createIndex(
      map,
      MongoDBObject("unique" -> true, "name" -> name)
    )
  }

  def addTextIndex(name: String, fields: String*) = {
    var map = MongoDBObject()
    fields.foreach { i => map.put(i, "text") }
    collection.createIndex(
      map,
      MongoDBObject("name" -> name)
    )
  }

  def dropIndex(name: String) = {
    collection.dropIndex(name)
  }

}
