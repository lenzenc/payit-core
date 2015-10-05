package com.payit.components.validation.rules

import com.payit.components.validation.{Failed, Success, Validated}

trait GeneralRules {

  type HasLength = Any { def length: Int }

  case class Required[T](allowBlank: Boolean = false) extends ValidationRule[T] {
    def apply(value: T): Validated[RuleFailure, T] = value match {
      case null => failed("required", "is required")
      case _ => if (!allowBlank && value.toString.trim.isEmpty) failed("blank", "must not be blank") else succeeded(value)
    }
  }

  case class MaxLength[T <% HasLength](max: Int) extends ValidationRule[T] {

    import scala.language.reflectiveCalls

    require(max >= 0, "max can not be less than ZERO")

    def apply(value: T): Validated[RuleFailure, T] = value match {
      case x if x != null && x.length > max => failed("maxlength", s"maximum is $max characters", Seq(max))
      case _ => succeeded(value)
    }
  }

  case class MinLength[T <% HasLength](min: Int) extends ValidationRule[T] {

    import scala.language.reflectiveCalls

    require(min >= 0, "min can not be less than ZERO")

    def apply(value: T): Validated[RuleFailure, T] = value match {
      case x if x == null || (x != null && x.length < min) => failed("minlength", s"minimum is $min characters", Seq(min))
      case _ => succeeded(value)
    }

  }

  case class BetweenLength[T <% HasLength](min: Int, max: Int) extends ValidationRule[T] {

    require(min >= 0, "min can not be less than ZERO")
    require(max >= 0, "max can not be less than ZERO")

    def apply(value: T): Validated[RuleFailure, T] = MinLength[T](min).apply(value) match {
      case Failed(f) => buildFailure
      case Success(_) => MaxLength[T](max).apply(value) match {
        case Failed(f) => buildFailure
        case Success(_) => succeeded(value)
      }
    }

    protected def buildFailure: Validated[RuleFailure, T] = {
      failed("betweenlength", s"should be between $min and $max characters", Seq(min, max))
    }

  }

  case object ValidEmail extends ValidationRule[String] {

    val ValidEmail = """(?i)\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,8}\b""".r

    def apply(value: String): Validated[RuleFailure, String] = {
      if (value != null && ValidEmail.pattern.matcher(value).matches) Success(value)
      else failed("invalidEmail", "is an invalid email")
    }

  }

}
