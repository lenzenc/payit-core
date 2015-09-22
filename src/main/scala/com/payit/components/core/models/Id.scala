package com.payit.components.core.models

abstract class Id[+T](value: T) {
  override def toString = value.toString
}
