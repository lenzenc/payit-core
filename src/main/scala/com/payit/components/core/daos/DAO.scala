package com.payit.components.core.daos

import com.payit.components.core.models.{Model, Id}

trait DAO[ID <: Id, M <: Model[ID, M]] {

  def insert(model: M): M

}
