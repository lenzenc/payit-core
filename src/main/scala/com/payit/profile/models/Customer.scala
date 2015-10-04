package com.payit.profile.models

import com.payit.components.core.models.{Model, Id, Timestamps}
import com.mongodb.casbah.Imports.ObjectId
import com.payit.components.mongo.models.{MongoModel, MongoId}
import org.joda.time.DateTime

case class CustomerId(value: ObjectId = new ObjectId) extends MongoId
case class Customer(
  id: Option[CustomerId],
  name: String,
  contact: Contact,
  domains: Seq[Domain],
  isActive: Boolean = true,
  timestamps: Timestamps = Timestamps())
extends MongoModel[CustomerId, Customer]
{

  def withId(idValue: ObjectId) = copy(id = Some(CustomerId(new ObjectId)))

  def withUpdatedAt(now: DateTime = DateTime.now) = copy(timestamps = timestamps.withUpdatedAt(now))

}


