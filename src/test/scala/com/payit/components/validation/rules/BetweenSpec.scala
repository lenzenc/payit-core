package com.payit.components.validation.rules

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class BetweenSpec extends Specification with ValidationMatchers with OrderingRules {

  ".apply" >> {
    val rule = Between[Int](1, 5)
    "it should allow values" >> {
      rule should passValues(1, 3, 5)
    }
    "it should not allow values" >> {
      rule should failValues(0, 6)
    }
    "when successful" >> {
      "it should return value under test" >> {
        rule(3) should beSuccessful(3)
      }
    }
    "when fails" >> {
      "it should have correct rule violation ruleKey" >> {
        rule(6) should beFailing.like { case f => f.ruleKey must_== "between" }
      }
      "it should have correct rule violation message" >> {
        rule(6) should beFailing.like { case f => f.message must_== "should be between 1 and 5" }
      }
      "it should have correct rule violation params" >> {
        rule(6) should beFailing.like { case f => f.params.asInstanceOf[Seq[Int]] should contain(exactly(1, 5)) }
      }
    }
    "when lower compare value is NULL" >> {
      "it should throw exception" >> {
        Between[String](null, "Upper") must throwA[IllegalArgumentException](message = "lower compareTo value can not be NULL")
      }
    }
    "when upper compare value is NULL" >> {
      "it should throw exception" >> {
        Between[String]("Lower", null) must throwA[IllegalArgumentException](message = "upper compareTo value can not be NULL")
      }
    }
  }

}