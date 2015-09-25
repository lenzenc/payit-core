package com.payit

import com.payit.components.core.Configuration
import com.payit.components.mongo.migrations.{MongoMigrator, MigrationCommand}

trait MongoMigrations {

  def config: Configuration

  def basePackage = "com.payit.data.migrations"
  def mongoDB = "default"
  private lazy val migrator = new MongoMigrator("default", config)

  def migrate(command: MigrationCommand) = {
    migrator.migrate(command, basePackage)
  }

}
