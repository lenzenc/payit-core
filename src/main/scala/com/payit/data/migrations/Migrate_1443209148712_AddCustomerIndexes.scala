package com.payit.data.migrations

import com.mongodb.casbah.Imports._
import com.payit.components.mongo.migrations.MongoMigration

class Migrate_1443209148712_AddCustomerIndexes(db: MongoDB) extends MongoMigration(db) {

  val collectionName = "customers"

  def up = {
    addUniqueIndex("UNIQ_NAME_IDX", "name")
  }

  def down = {
    dropIndex("UNIQ_NAME_IDX")
  }

}