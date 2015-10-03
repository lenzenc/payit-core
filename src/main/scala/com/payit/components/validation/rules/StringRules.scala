package com.payit.components.validation.rules

import com.payit.components.validation.Validated

trait StringRules {

  case class StartsWith(str: String) extends ValidationRule[String] {
    def apply(value: String): Validated[RuleFailure, String] = value match {
      case s if s != null && s.startsWith(str) => succeeded(value)
      case _ => failed("startswith", s"should start with $value", Seq(value))
    }
  }

}
