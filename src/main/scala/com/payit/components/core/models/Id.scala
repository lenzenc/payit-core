package com.payit.components.core.models

//abstract class Id[+T](value: T) {
//  override def toString = value.toString
//}

trait Id {

  type IdValue

  def value: IdValue

  override def toString = value.toString

}
