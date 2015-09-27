package com.payit.profile.models

import com.mongodb.casbah.Imports.ObjectId
import com.payit.components.core.models.Timestamps
import com.payit.components.mongo.models.{MongoId, MongoModel}

case class UserId(value: ObjectId) extends MongoId

case class User(
  id: Option[UserId],
  firstName: String,
  mi: Option[String],
  lastName: String,
  loginId: String,
  password: String,
  email: String,
  customerId: CustomerId,
  isActive: Boolean = true,
  timestamps: Timestamps = Timestamps())
extends MongoModel[UserId, User]
{

  def withId(idValue: ObjectId) = copy(id = Some(UserId(idValue)))

}
