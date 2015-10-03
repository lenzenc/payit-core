package com.payit.components.validation.rules

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class LessThanOrEqualSpec extends Specification with ValidationMatchers with OrderingRules {

  ".apply" >> {
    val rule = LessThanOrEqual[Int](5)
    "it should allow values" >> {
      rule should passValues(5, 4, 3, 2)
    }
    "it should not allow values" >> {
      rule should failValues(6, 7)
    }
    "when successful" >> {
      "it should return value under test" >> {
        rule(4) should beSuccessful(4)
      }
    }
    "when fails" >> {
      "it should have correct rule violation ruleKey" >> {
        rule(6) should beFailing.like { case f => f.ruleKey must_== "lessthanorequal" }
      }
      "it should have correct rule violation message" >> {
        rule(6) should beFailing.like { case f => f.message must_== "should be less than or equal to 5" }
      }
      "it should have correct rule violation params" >> {
        rule(6) should beFailing.like { case f => f.params.asInstanceOf[Seq[Int]] should contain(exactly(5)) }
      }
    }
    "when rule value is NULL" >> {
      "it should fail" >> {
        val rule = LessThanOrEqual[String]("Testing")
        rule(null) should beFailing
      }
    }
    "when compare value is NULL" >> {
      "it should throw exception" >> {
        LessThanOrEqual[String](null) must throwA[IllegalArgumentException](message = "compareTo value can not be NULL")
      }
    }
  }

}