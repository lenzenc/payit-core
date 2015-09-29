package com.payit

import com.payit.components.core.Configuration
import com.payit.components.mongo.migrations.{MongoMigrator, MigrationCommand}

trait MongoMigrations {

  def config: Configuration

  def basePackage = "com.payit.data.migrations"
  def dbConfigName: String
  private lazy val migrator = new MongoMigrator(dbConfigName, config)

  def migrate(command: MigrationCommand) = {
    migrator.migrate(command, basePackage)
  }

}
