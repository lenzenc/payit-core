package com.payit.components.mongo.daos

import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import com.payit.components.core.models.Timestamps
import com.payit.components.mongo.models.{MongoId, MongoModel}
import org.joda.time.DateTime
import com.mongodb.casbah.Imports._

trait DocumentMapper[ID <: MongoId, M <: MongoModel[ID, M]] {

  com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers()

  val Id = "_id"
  val Timestamps = "timestamps"
  val CreatedAt = "createdAt"
  val UpdatedAt = "updatedAt"

  def asDBObject(model: M): DBObject

  def fromDBObject(dbo: DBObject): M

  protected def id(model: M) = {
    Id -> model.id.getOrElse(sys.error(s"Model: ${model.getClass().getName} does not yet have an id!")).value
  }

  protected def id(dbo: DBObject): ObjectId = dbo.as[ObjectId](Id)

  protected def timestamps(model: M) = {
    Timestamps -> MongoDBObject(
      CreatedAt -> model.timestamps.createdAt,
      UpdatedAt -> model.timestamps.updatedAt
    )
  }

  protected def timestamps(dbo: DBObject): Timestamps = {
    com.payit.components.core.models.Timestamps(
      createdAt = dbo.as[DBObject](Timestamps).as[DateTime](CreatedAt),
      updatedAt = dbo.as[DBObject](Timestamps).as[DateTime](UpdatedAt)
    )
  }

}
