package com.payit.components.validation

case class ParentKey(keys: String*) {
  override def toString() = keys.mkString("->")
}
