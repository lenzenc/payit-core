package com.payit.components.validation

trait Validations[T] { obj: T =>

  protected var validator: Validator[T] = new Validator[T] {
    override val parentKey = ParentKey(validationKey)
    val ruleSets = Seq.empty
  }

  def isValid: Validated[Map[FailureKey, Seq[ValidationFailure]], T] = {
    validator(obj)
  }

  protected def validations(validationRuleSets: ValidationRuleSet[T, _]*) = {
    validator = new Validator[T] {
      override val parentKey = ParentKey(validationKey)
      val ruleSets = validationRuleSets
    }
  }

  protected def prop[V](key: String, prop: (T) => V, parentKey: ParentKey = ParentKey()) = {
    ValidationProp[T, V](key, prop, parentKey)
  }

  protected def validationKey = obj.getClass().getSimpleName

}
