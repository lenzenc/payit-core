package com.payit.components.validation

import org.specs2.mutable.Specification

class ValidatedSpec extends Specification {

  val SuccessValidation: Validated[String, String] = Success("success")
  val FailedValidation: Validated[String, String] = Failed("failed")

  ".isSuccess" >> {
    "when Success" >> {
      "it should return true" >> {
        SuccessValidation.isSuccess should beTrue
      }
    }
    "when Failed" >> {
      "it should return false" >> {
        FailedValidation.isSuccess should beFalse
      }
    }
  }

  ".isFailure" >> {
    "when Success" >> {
      "it should return false" >> {
        SuccessValidation.isFailure should beFalse
      }
    }
    "when Failed" >> {
      "it should return true" >> {
        FailedValidation.isFailure should beTrue
      }
    }
  }

}
