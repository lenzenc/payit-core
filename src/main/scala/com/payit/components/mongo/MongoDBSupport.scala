package com.payit.components.mongo

import com.mongodb.casbah.Imports._
import com.payit.components.core.Configuration

trait MongoDBSupport {

  def mongoDB: MongoDB = client(config.getString(s"mongo.$dbConfigName.dbname").getOrElse(
    sys.error(s"Unable to find dbname configuration for config: ${s"mongo.$dbConfigName.dbname"}"))
  )

  protected def config: Configuration
  protected def dbConfigName: String
  protected lazy val client = buildClient

  protected def buildClient: MongoClient = {
    MongoClient(
      buildServer,
      buildCredentials
    )
  }

  protected def buildServer: ServerAddress = {
    new ServerAddress(
      config.getString(s"mongo.$dbConfigName.host").getOrElse("localhost"),
      config.getInt(s"mongo.$dbConfigName.port").getOrElse(27017)
    )
  }

  protected def buildCredentials: List[MongoCredential] = {
    val username = config.getString(s"mongo.$dbConfigName.username")
    val password = config.getString(s"mongo.$dbConfigName.password")
    if (username.isDefined && password.isDefined) List(MongoCredential.createCredential(
      username.get,
      dbConfigName,
      password.get.toArray))
    else List.empty[MongoCredential]
  }

}
