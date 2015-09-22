package com.payit.components.core.models

trait Model[ID, M <: Model[ID, M]] extends Identifiable[ID, M] {

  def timestamps: Timestamps

}
