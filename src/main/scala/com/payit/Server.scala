package com.payit

import com.payit.components.core.Configuration
import com.payit.components.mongo.migrations.{ApplyMigrations, ResetApplyMigrations, MigrationCommand}
import com.typesafe.scalalogging.LazyLogging

object Server extends App with LazyLogging with MongoMigrations {

  logger.debug("Initializing & Running Payit!....")

  lazy val config: Configuration = Configuration.load
  lazy val command: MigrationCommand = args match {
    case Array(a) if a.equals("reset") => ResetApplyMigrations
    case _ => ApplyMigrations
  }

  migrate(command)

}
