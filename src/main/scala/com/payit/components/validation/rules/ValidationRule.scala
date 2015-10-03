package com.payit.components.validation.rules

import com.payit.components.validation.{Success, Failed, Validated}

trait ValidationRule[T] extends (T => Validated[RuleFailure, T]) {

  def succeeded(value: T): Validated[RuleFailure, T] = Success(value)

  def failed(ruleKey: String, message: String, params: Seq[Any] = Seq.empty): Validated[RuleFailure, T] = {
    Failed(RuleFailure(ruleKey, message, params))
  }

}
