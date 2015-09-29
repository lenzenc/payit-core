package com.payit

import com.payit.components.core.Configuration
import com.payit.components.mongo.migrations.{MongoMigrator, MigrationCommand}
import com.typesafe.scalalogging.LazyLogging

trait MongoMigrations extends LazyLogging {

  def config: Configuration

  def basePackage = "com.payit.data.migrations"
  def dbConfigName: String
  private lazy val migrator = new MongoMigrator(dbConfigName, config)

  def migrate(command: MigrationCommand) = {
    logger.debug(s"Running Mongo Migration Command: $command")
    migrator.migrate(command, basePackage)
  }

}
