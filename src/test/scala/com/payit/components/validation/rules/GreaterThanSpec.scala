package com.payit.components.validation.rules

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class GreaterThanSpec extends Specification with ValidationMatchers with OrderingRules {

  ".apply" >> {
    val rule = GreaterThan[Int](5)
    "it should allow values" >> {
      rule should passValues(6, 7, 10)
    }
    "it should not allow values" >> {
      rule should failValues(4, 3)
    }
    "when successful" >> {
      "it should return value under test" >> {
        rule(10) should beSuccessful(10)
      }
    }
    "when fails" >> {
      "it should have correct rule violation ruleKey" >> {
        rule(2) should beFailing.like { case f => f.ruleKey must_== "greaterthan" }
      }
      "it should have correct rule violation message" >> {
        rule(2) should beFailing.like { case f => f.message must_== "should be greater than 5" }
      }
      "it should have correct rule violation params" >> {
        rule(2) should beFailing.like { case f => f.params.asInstanceOf[Seq[Int]] should contain(exactly(5)) }
      }
    }
    "when rule value is NULL" >> {
      "it should fail" >> {
        val rule = GreaterThan[String]("Testing")
        rule(null) should beFailing
      }
    }
    "when compare value is NULL" >> {
      "it should throw exception" >> {
        GreaterThan[String](null) must throwA[IllegalArgumentException](message = "compareTo value can not be NULL")
      }
    }
  }

}
