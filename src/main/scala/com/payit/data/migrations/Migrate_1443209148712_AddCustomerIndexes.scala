package com.payit.data.migrations

import com.mongodb.casbah.Imports._
import com.payit.components.mongo.migrations.MongoMigration
import com.payit.data.Collections

class Migrate_1443209148712_AddCustomerIndexes(db: MongoDB) extends MongoMigration(db) {

  val collectionName = Collections.Customers.toString

  def up = {
    addUniqueIndex("UNIQ_NAME_IDX", "name")
    addTextIndex("NAME_TEXT_IDX", "name")
    addUniqueIndex("UNIQ_DOMAIN_IDX", "domain")
    addUniqueIndex("UNIQ_ADD_DOMAIN_IDX", "additionalDomains.domain")
  }

  def down = {
    dropIndex("UNIQ_NAME_IDX")
    dropIndex("NAME_TEXT_IDX")
    dropIndex("UNIQ_DOMAIN_IDX")
    dropIndex("UNIQ_ADD_DOMAIN_IDX")
  }

}