package com.payit.components.core.models

trait Model[ID <: Id, M <: Model[ID, M]] {

  def id: Option[ID]

  def timestamps: Timestamps

  def withId(idValue: ID#IdValue): M

}
