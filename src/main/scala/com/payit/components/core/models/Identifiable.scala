package com.payit.components.core.models

trait Identifiable[ID, E <: Identifiable[ID, E]] {

  def id: Option[Id[ID]]

}
