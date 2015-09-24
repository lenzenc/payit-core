package com.payit.components.mongo.migrations

import com.mongodb.casbah.{MongoDB, MongoCollection}
import com.mongodb.casbah.commons.MongoDBObject

trait MongoMigration {

  def up(db: MongoDB)

  def down(db: MongoDB)

  def addUniqueIndex(collection: MongoCollection, name: String, fields: String*) = {
    var map = MongoDBObject()
    fields.foreach { i => map.put(i, 1) }
    collection.createIndex(
      map,
      MongoDBObject("unique" -> true, "name" -> name)
    )
  }

  def dropIndex(collection: MongoCollection, name: String) = {
    collection.dropIndex(name)
  }

}
