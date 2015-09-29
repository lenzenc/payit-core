package com.payit

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.payit.components.core.Configuration
import com.payit.components.mongo.migrations.{ApplyMigrations, ResetApplyMigrations, MigrationCommand}
import com.typesafe.scalalogging.LazyLogging
import spray.can.Http

import scala.util.Properties

object Server extends App with LazyLogging with MongoMigrations {

  logger.debug("Initializing & Running Payit!....")

  lazy val config: Configuration = Configuration.load
  val dbConfigName = Properties.envOrElse("ENVIRONMENT", "dev").toLowerCase
  logger.debug(s"Using DB Environment Config: $dbConfigName")
  lazy val command: MigrationCommand = args match {
    case Array(a) if a.equals("reset") => ResetApplyMigrations
    case _ => ApplyMigrations
  }

  logger.debug(s"Running Mongo Migration Command: $command")
  migrate(command)

  implicit val actorSystem = ActorSystem()
  val service = actorSystem.actorOf(Props[SprayActor], "payit-core")
  val port = Properties.envOrElse("PORT", "9001").toInt
  IO(Http)(actorSystem) ! Http.Bind(service, "0.0.0.0", port = port)
  sys.addShutdownHook(actorSystem.shutdown())

}
