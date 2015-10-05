package com.payit.components.validation.rules

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class ValidEmailSpec extends Specification with GeneralRules with ValidationMatchers {

  ".apply" >> {
    val rule = ValidEmail
    "it should allow values" >> {
      rule should passValues(
        "bob@gmail.com",
        "bob@gmail.net",
        "bob@gmail.org",
        "bob@gmail.foobar",
        "b@gmail.com",
        "bob@mycompany.com"
      )
    }
    "it should not allow values" >> {
      rule should failValues(
        null,
        "",
        "gmail.com",
        "bob",
        "bob.com",
        "com",
        ".com",
        "bob@gmail"
      )
    }
    "when Success" >> {
      "it should return value under test" >> {
        rule("bob@gmail.com") should beSuccessful("bob@gmail.com")
      }
    }
    "when Failed" >> {
      "it should have correct rule violation ruleKey" >> {
        rule("invalidValue") should beFailing.like { case f => f.ruleKey must_== "invalidEmail" }
      }
      "it should have correct rule violation message" >> {
        rule("invalidValue") should beFailing.like { case f => f.message must_== "is an invalid email" }
      }
      "it should have correct rule violation params" >> {
        rule("invalidValue") should beFailing.like { case f => f.params should beEmpty }
      }
    }
  }

}
