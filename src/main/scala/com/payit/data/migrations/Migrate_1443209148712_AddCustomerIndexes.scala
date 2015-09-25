package com.payit.data.migrations

import com.mongodb.casbah.Imports._
import com.payit.components.mongo.migrations.MongoMigration

class Migrate_1443209148712_AddCustomerIndexes extends MongoMigration {

  def up(db: MongoDB) = {
    val collection = db("customers")
    addUniqueIndex(collection, "UNIQ_NAME_IDX", "name")
  }

  def down(db: MongoDB) = {
    val collection = db("customers")
    dropIndex(collection, "UNIQ_NAME_IDX")
  }

}