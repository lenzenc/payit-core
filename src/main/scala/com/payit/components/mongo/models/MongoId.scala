package com.payit.components.mongo.models

import com.payit.components.core.models.Id
import com.mongodb.casbah.Imports.ObjectId

trait MongoId extends Id {

  type IdValue = ObjectId

}
