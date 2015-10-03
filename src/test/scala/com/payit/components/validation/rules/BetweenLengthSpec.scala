package com.payit.components.validation.rules

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class BetweenLengthSpec extends Specification with GeneralRules with ValidationMatchers {

  ".apply" >> {
    val rule = BetweenLength[String](min = 2, max = 4)
    "it should allow values" >> {
      rule should passValues("TT", "TTT", "TTTT")
    }
    "it should not allow values" >> {
      rule should failValues(null, "T", "TTTTT")
    }
    "when successful" >> {
      "it should return value under test" >> {
        rule("TT") should beSuccessful("TT")
      }
    }
    "when fails" >> {
      "it should have correct rule violation ruleKey" >> {
        rule("T") should beFailing.like { case f => f.ruleKey must_== "betweenlength" }
      }
      "it should have correct rule violation message" >> {
        rule("T") should beFailing.like { case f => f.message must_== "should be between 2 and 4 characters" }
      }
      "it should have correct rule violation params" >> {
        rule("T") should beFailing.like { case f => f.params.asInstanceOf[Seq[Int]] should contain(exactly(2, 4)) }
      }
    }
    "when min is set to NON ZERO" >> {
      "it should result in an error" >> {
        BetweenLength[String](-1, 10) must throwA[IllegalArgumentException](message = "min can not be less than ZERO")
      }
    }
    "when max is set to NON ZERO" >> {
      "it should result in an error" >> {
        BetweenLength[String](1, -1) must throwA[IllegalArgumentException](message = "max can not be less than ZERO")
      }
    }
  }

}