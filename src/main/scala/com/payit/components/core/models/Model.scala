package com.payit.components.core.models

import com.payit.components.validation.Validations

trait Model[ID <: Id, M <: Model[ID, M]] extends Validations {

  def id: Option[ID]

  def timestamps: Timestamps

  def withId(idValue: ID#IdValue): M

}
