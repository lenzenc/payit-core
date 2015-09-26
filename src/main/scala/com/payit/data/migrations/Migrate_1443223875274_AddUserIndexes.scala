package com.payit.data.migrations

import com.mongodb.casbah.Imports._
import com.payit.components.mongo.migrations.MongoMigration
import com.payit.data.Collections

class Migrate_1443223875274_AddUserIndexes(db: MongoDB) extends MongoMigration(db) {

  val collectionName = Collections.Users.toString

  def up = {
    addUniqueIndex("UNIQ_LOGIN_IDX", "loginId")
    addTextIndex("NAME_TEXT_IDX", "firstName", "lastName")
  }

  def down = {
    dropIndex("UNIQ_LOGIN_IDX")
    dropIndex("NAME_TEXT_IDX")
  }

}