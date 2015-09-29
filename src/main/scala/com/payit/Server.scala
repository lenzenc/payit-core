package com.payit

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.payit.components.core.Configuration
import com.payit.components.mongo.migrations.{ApplyMigrations, ResetApplyMigrations, MigrationCommand}
import com.typesafe.scalalogging.LazyLogging
import spray.can.Http

object Server extends App with LazyLogging {

  logger.debug("Initializing & Running Payit!....")

//  lazy val config: Configuration = Configuration.load
//  lazy val command: MigrationCommand = args match {
//    case Array(a) if a.equals("reset") => ResetApplyMigrations
//    case _ => ApplyMigrations
//  }
//
//  migrate(command)

  implicit val actorSystem = ActorSystem()
  val service = actorSystem.actorOf(Props[SprayActor], "payit-core")
  IO(Http)(actorSystem) ! Http.Bind(service, "0.0.0.0", port = 9001)
  sys.addShutdownHook(actorSystem.shutdown())

}
