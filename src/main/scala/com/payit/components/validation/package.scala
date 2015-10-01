package com.payit.components

import com.payit.components.validation.rules.{ValidationRule, StringRules, OrderingRules, GeneralRules}

package object validation extends GeneralRules with OrderingRules with StringRules {

  def validator[T](key: String, validationRuleSets: ValidationRuleSet[T, _]*): Validator[T] = new Validator[T] {
    override val parentKey = ParentKey(key)
    val ruleSets = validationRuleSets
  }

  case class ValidationProp[T, V](key: String, prop: (T) => V)

  implicit class RuleOpts[T, V](validationProp: ValidationProp[T, V]) {
    def is(validationRules: ValidationRule[V]*): ValidationRuleSet[T, V] = new ValidationRuleSet[T, V] {
      val parentKey = ParentKey()
      val key = validationProp.key
      val mapper = validationProp.prop
      val rules = validationRules
    }
  }

  def prop[T, V](key: String, prop: (T) => V) = ValidationProp[T, V](key, prop)

}
