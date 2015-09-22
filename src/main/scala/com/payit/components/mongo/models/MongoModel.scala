package com.payit.components.mongo.models

import com.mongodb.casbah.Imports.ObjectId
import com.payit.components.core.models.Model

trait MongoModel[M <: MongoModel[M]] extends Model[ObjectId, M]