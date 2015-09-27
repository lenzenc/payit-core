package com.payit.components.mongo.models

import com.payit.components.core.models.Model

trait MongoModel[ID <: MongoId, M <: MongoModel[ID, M]] extends Model[ID, M]