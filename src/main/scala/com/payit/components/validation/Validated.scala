package com.payit.components.validation

sealed trait Validated[+A, +B] {

  def isSuccess: Boolean = this match {
    case Success(_) => true
    case Failed(_) => false
  }

  def isFailure: Boolean = this match {
    case Success(_) => false
    case Failed(_) => true
  }

}
final case class Failed[A](a: A) extends Validated[A, Nothing]
final case class Success[B](b: B) extends Validated[Nothing, B]
