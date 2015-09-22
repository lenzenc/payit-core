package com.payit.profile.models

import com.payit.components.core.models.{Id, Timestamps}
import com.mongodb.casbah.Imports.ObjectId
import com.payit.components.mongo.models.MongoModel

case class CustomerId(value: ObjectId) extends Id(value)

case class Customer(
  id: Option[CustomerId],
  name: String,
  contact: Contact,
  domains: Seq[Domain],
  isActive: Boolean = true,
  timestamps: Timestamps = Timestamps())
extends MongoModel[Customer]
