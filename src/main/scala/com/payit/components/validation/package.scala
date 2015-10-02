package com.payit.components

import com.payit.components.validation.rules.{ValidationRule, StringRules, OrderingRules, GeneralRules}

package object validation extends GeneralRules with OrderingRules with StringRules {

  case class ValidationProp[T, V](key: String, prop: (T) => V, parentKey: ParentKey = ParentKey())

  implicit class RuleOpts[T, V](validationProp: ValidationProp[T, V]) {
    def is(validationRules: ValidationRule[V]*): ValidationRuleSet[T, V] = new ValidationRuleSet[T, V] {
      val parentKey = validationProp.parentKey
      val key = validationProp.key
      val mapper = validationProp.prop
      val rules = validationRules
    }
  }

}
