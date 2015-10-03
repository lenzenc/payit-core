package com.payit.components.validation.rules

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class MaxLengthSpec extends Specification with GeneralRules with ValidationMatchers {

  ".apply" >> {
    val rule = MaxLength[String](max = 2)
    "it should allow values" >> {
      rule should passValues("a", "ab", null, "")
    }
    "it should not allow values" >> {
      rule should failValues("abc")
    }
    "when successful" >> {
      "it should return value under test" >> {
        rule("ab") should beSuccessful("ab")
      }
    }
    "when fails" >> {
      "it should have correct rule violation ruleKey" >> {
        rule("abc") should beFailing.like { case f => f.ruleKey must_== "maxlength" }
      }
      "it should have correct rule violation message" >> {
        rule("abc") should beFailing.like { case f => f.message must_== "maximum is 2 characters" }
      }
      "it should have correct rule violation params" >> {
        rule("abc") should beFailing.like { case f => f.params.asInstanceOf[Seq[Int]] should contain(exactly(2)) }
      }
    }
    "when max is set to ZERO" >> {
      "it should fail validation when value is greater than ZERO" >> {
        MaxLength[String](max = 0) should failValues("a")
      }
    }
    "when max is set to NON ZERO" >> {
      "it should error out" >> {
        MaxLength[String](max = -1) must throwA[IllegalArgumentException](message = "max can not be less than ZERO")
      }
    }
  }

}

