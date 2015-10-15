package com.payit.components.mongo.daos

import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.MongoDBObject
import com.payit.components.core.daos.DAO
import com.payit.components.core.models.ModelValidationException
import com.payit.components.mongo.models.{MongoModel, MongoId}
import com.mongodb.casbah.Imports.ObjectId
import com.payit.validations.{Failed, Success}

trait MongoDAO[ID <: MongoId, M <: MongoModel[ID, M]] extends DAO[ID, M] {

  protected def mapper: DocumentMapper[ID, M]
  protected val collection: MongoCollection

  def insert(model: M): M = {
    val modelWithId = if (model.id.isEmpty) model.withId(new ObjectId) else model
    modelWithId.isValid match {
      case Success(_) =>
        collection.insert(mapper.asDBObject(modelWithId))
        modelWithId
      case Failed(failures) => throw new ModelValidationException(failures)
    }
  }

  def update(model: M): M = {
    if (model.id.isEmpty) sys.error(s"Unable to update ${model.getClass.getName} because id is None!")
    var updatedModel = model.withUpdatedAt()
    model.isValid match {
      case Success(_) =>
        collection.update(
          MongoDBObject("_id" -> updatedModel.id.get.value),
          mapper.asDBObject(updatedModel))
        updatedModel
      case Failed(failures) => throw new ModelValidationException(failures)
    }
  }

}
