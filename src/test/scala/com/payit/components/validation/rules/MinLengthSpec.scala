package com.payit.components.validation.rules

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class MinLengthSpec extends Specification with GeneralRules with ValidationMatchers {

  ".apply" >> {
    val rule = MinLength[String](2)
    "it should allow values" >> {
      rule should passValues("TT", "TTT")
    }
    "it should not allow values" >> {
      rule should failValues(null, "T")
    }
    "when successful" >> {
      "it should return value under test" >> {
        rule("TT") should beSuccessful("TT")
      }
    }
    "when fails" >> {
      "it should have correct rule violation ruleKey" >> {
        rule("T") should beFailing.like { case f => f.ruleKey must_== "minlength" }
      }
      "it should have correct rule violation message" >> {
        rule("T") should beFailing.like { case f => f.message must_== "minimum is 2 characters" }
      }
      "it should have correct rule violation params" >> {
        rule("T") should beFailing.like { case f => f.params.asInstanceOf[Seq[Int]] should contain(exactly(2)) }
      }
    }
    "when min is set to NON ZERO" >> {
      "it should result in an error" >> {
        MinLength[String](-1) must throwA[IllegalArgumentException](message = "min can not be less than ZERO")
      }
    }
    "when min is set to ZERO" >> {
      "it should result in a success when value is anything other than null" >> {
        val rule = MinLength[String](0)
        rule("T") should beSuccessful
      }
      "it should result in a failure when value is null" >> {
        MinLength[String](0) should failValues(null)
      }
    }
  }

}
