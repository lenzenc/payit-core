package com.payit.components.validation

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class ValidationsSpec extends Specification with ValidationMatchers {

  case class NoValidationsSpecObj() extends Validations[NoValidationsSpecObj] {
    def getValidator = validator
  }

  case class ValidationSpecObj(v1: String, v2: String) extends Validations[ValidationSpecObj] {
    def getValidator = validator
    validations(
      prop("v1", { v => v.v1 }).is(Required()),
      prop("v2", { v => v.v2 }, ParentKey("parent")).is(Required())
    )
  }

  "it should have Validator key matching the mixed in class name" >> {
    ValidationSpecObj("val1", "val2").getValidator.parentKey must_== ParentKey("ValidationSpecObj")
  }
  "when .validations is never called" >> {
    "it should have a skeleton Validator with no rule sets" >> {
      NoValidationsSpecObj().getValidator.ruleSets.size must_== 0
    }
    "it shojld have a skeleton Validator with key matching the mixed in class name" >> {
      NoValidationsSpecObj().getValidator.parentKey must_== ParentKey("NoValidationsSpecObj")
    }
  }
  ".validations" >> {
    "when prop is NOT mapped with a parent key" >> {
      "it should contain a ValidationRuleSet with expected key" >> {
        specObject().getValidator.ruleSets must have size(2)
        specObject().getValidator.ruleSets(0).key must_== "v1"
      }
      "it should contain a ValidationRuleSet with expected parent key" >> {
        specObject().getValidator.ruleSets must have size(2)
        specObject().getValidator.ruleSets(0).parentKey must_== ParentKey()
      }
      "it should contain a ValidationRuleSet with expected ValidationRule" >> {
        specObject().getValidator.ruleSets must have size(2)
        specObject().getValidator.ruleSets(0).rules must have size(1)
        specObject().getValidator.ruleSets(0).rules(0) must beAnInstanceOf[Required[String]]
      }
    }
    "when prop IS mapped with a parent key" >> {
      "it should contain a ValidationRuleSet with expected key" >> {
        specObject().getValidator.ruleSets must have size(2)
        specObject().getValidator.ruleSets(1).key must_== "v2"
      }
      "it should contain a ValidationRuleSet with expected parent key" >> {
        specObject().getValidator.ruleSets must have size(2)
        specObject().getValidator.ruleSets(1).parentKey must_== ParentKey("parent")
      }
      "it should contain a ValidationRuleSet with expected ValidationRule" >> {
        specObject().getValidator.ruleSets must have size(2)
        specObject().getValidator.ruleSets(1).rules must have size(1)
        specObject().getValidator.ruleSets(1).rules(0) must beAnInstanceOf[Required[String]]
      }
    }
  }
  ".isValid" >> {
    "it should return Success for an object that passes all validations" >> {
      specObject().isValid must beSuccessful
    }
    "it should return Failed for an object that does not pass all validations" >> {
      specObject(v1 = "").isValid must beFailing.like{ case map => map must haveKey(FailureKey("v1", ParentKey())) }
    }
  }

  private def specObject(v1: String = "v1", v2: String = "v2") = ValidationSpecObj(v1, v2)

}
