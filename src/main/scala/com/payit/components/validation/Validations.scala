package com.payit.components.validation

trait Validations { obj =>

  protected var validator: Validator[this.type] = new Validator[this.type] {
    override val parentKey = ParentKey(validationKey)
    val ruleSets = Seq.empty
  }

  def isValid: Validated[Map[FailureKey, Seq[ValidationFailure]], this.type] = {
    validator(obj)
  }

  protected def validations(validationRuleSets: ValidationRuleSet[this.type, _]*) = {
    validator = new Validator[this.type] {
      override val parentKey = ParentKey(validationKey)
      val ruleSets = validationRuleSets
    }
  }

  protected def prop[V](key: String, prop: (this.type) => V, parentKey: ParentKey = ParentKey()) = {
    ValidationProp[this.type, V](key, prop, parentKey)
  }

  protected def validationKey = obj.getClass().getSimpleName

}
