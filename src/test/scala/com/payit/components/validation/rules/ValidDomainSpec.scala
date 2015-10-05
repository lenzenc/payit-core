package com.payit.components.validation.rules

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class ValidDomainSpec extends Specification with GeneralRules with ValidationMatchers {

  ".apply" >> {
    val rule = ValidDomain
    "it should allow values" >> {
      rule should passValues(
        "gmail.com",
        "gmail.net",
        "gmail.org",
        "gmail.foobar",
        "mycompany.com"
      )
    }
    "it should not allow values" >> {
      rule should failValues(
        null,
        "",
        "@gmail.com",
        "bob",
        "com",
        ".com",
        "bob@gmail",
        "bob@gmail.com"
      )
    }
    "when Success" >> {
      "it should return value under test" >> {
        rule("gmail.com") should beSuccessful("gmail.com")
      }
    }
    "when Failed" >> {
      "it should have correct rule violation ruleKey" >> {
        rule("invalidValue") should beFailing.like { case f => f.ruleKey must_== "invalidDomain" }
      }
      "it should have correct rule violation message" >> {
        rule("invalidValue") should beFailing.like { case f => f.message must_== "is an invalid domain" }
      }
      "it should have correct rule violation params" >> {
        rule("invalidValue") should beFailing.like { case f => f.params should beEmpty }
      }
    }
  }

}
