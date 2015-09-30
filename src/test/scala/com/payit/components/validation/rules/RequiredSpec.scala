package com.payit.components.validation.rules

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class RequiredSpec extends Specification with GeneralRules with ValidationMatchers {

  val RequiredKey = "required"
  val BlankKey = "blank"
  val RequiredMsg = "is required"
  val BlankMsg = "must not be blank"

  ".apply" >> {
    "using default constructor" >> {
      val rule = Required[String]()
      "it should allow values" >> {
        rule should passValues("value", " leadingValue", "trailingValue ", " leadingtrailingValue ")
      }
      "it should not allow values" >> {
        rule should failValues(null, "", " ", "  ")
      }
      "when value is NULL" >> {
        "it should have expected RuleViolation" >> {
          rule(null) should beFailing.like { case f =>
            f.ruleKey must_== RequiredKey
            f.message must_== RequiredMsg
          }
          rule(null) should beFailing(RuleFailure(RequiredKey, RequiredMsg))
        }
      }
      "when value is blank" >> {
        "it should have expected RuleViolation" >> {
          rule("") should beFailing(RuleFailure(BlankKey, BlankMsg))
        }
      }
    }
    "using constructor with allowedBlank equals false" >> {
      val rule = Required[String](allowBlank = false)
      "it should allow values" >> {
        rule should passValues("value", " leadingValue", "trailingValue ", " leadingtrailingValue ")
      }
      "it should not allow values" >> {
        rule should failValues(null, "", " ", "  ")
      }
      "when value is NULL" >> {
        "it should have expected RuleViolation" >> {
          rule(null) should beFailing(RuleFailure(RequiredKey, RequiredMsg))
        }
      }
      "when value is blank" >> {
        "it should have expected RuleViolation" >> {
          rule("") should beFailing(RuleFailure(BlankKey, BlankMsg))
        }
      }
    }
    "using constructor with allowedBlank equals true" >> {
      val rule = Required[String](allowBlank = true)
      "it should allow values" >> {
        rule should passValues("value", "", " ", " leadingValue", "trailingValue ", " leadingtrailingValue ")
      }
      "it should not allow values" >> {
        rule should failValues(null)
      }
      "when value is NULL" >> {
        "it should have expected RuleViolation" >> {
          rule(null) should beFailing(RuleFailure(RequiredKey, RequiredMsg))
        }
      }
    }
  }

}
