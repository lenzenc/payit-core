package com.payit.components.validation

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class ValidatorSpec extends Specification with ValidationMatchers with Mockito {

  case class TestModel(v1: String = "Value1", v2: String = "Value2")
  val model = TestModel()

  ".apply" >> {
    "when validation is successful" >> {
      "it should return object being validated" >> {
        val ruleSet = mock[ValidationRuleSet[TestModel, String]]
        ruleSet.apply(model) returns Success[TestModel](model)
        validator(ruleSet)(model) should beSuccessful(model)
      }
    }
    "when validation fails" >> {
      "using one ValidationRuleSet" >> {
        "with no ParentKey" >> {
          val ruleSet = mockFailedRuleSet("v1", "rule1", "msg1", model)
          "it should return expected ValidationFailure" >> {
            validator(ruleSet)(model) should beFailing(Map(
              FailureKey("v1") -> Seq(ValidationFailure(ParentKey(), "v1", "rule1", "msg1"))
            ))
          }
        }
        "with a ParentKey" >> {
          val ruleSet = mockFailedRuleSet("v1", "rule1", "msg1", model, ParentKey("parent"))
          "it should return expected ValidationFailure" >> {
            validator(ruleSet)(model) should beFailing(Map(
              FailureKey("v1", ParentKey("parent")) -> Seq(ValidationFailure(ParentKey("parent"), "v1", "rule1", "msg1"))
            ))
          }
        }
      }
      "using 2 ValidationRuleSets" >> {
        "when both do not have a ParentKey" >> {
          "and the keys are different" >> {
            val ruleSet1 = mockFailedRuleSet("v1", "rule1", "msg1", model)
            val ruleSet2 = mockFailedRuleSet("v2", "rule1", "msg1", model)
            "it should return expected ValidationFailures" >> {
              validator(ruleSet1, ruleSet2)(model) should beFailing(Map(
                FailureKey("v1") -> Seq(ValidationFailure(ParentKey(), "v1", "rule1", "msg1")),
                FailureKey("v2") -> Seq(ValidationFailure(ParentKey(), "v2", "rule1", "msg1"))
              ))
            }
          }
          "and the keys are the same" >> {
            val ruleSet1 = mockFailedRuleSet("v1", "rule1", "msg1", model)
            val ruleSet2 = mockFailedRuleSet("v1", "rule2", "msg2", model)
            "but each uses a different ValidationRule" >> {
              "it should return 1 ValidationFailure with a combination of both ValidationRule failures" >> {
                validator(ruleSet1, ruleSet2)(model) should beFailing(Map(
                  FailureKey("v1") -> Seq(
                    ValidationFailure(ParentKey(), "v1", "rule1", "msg1"),
                    ValidationFailure(ParentKey(), "v1", "rule2", "msg2")
                  )
                ))
              }
            }
            "but each uses the same ValidationRule" >> {
              val ruleSet1 = mockFailedRuleSet("v1", "rule1", "msg1", model)
              val ruleSet2 = mockFailedRuleSet("v1", "rule1", "msg1", model)
              "it should return 1 ValidationFailure with out duplicating a ValidationRule failure" >> {
                validator(ruleSet1, ruleSet2)(model) should beFailing(Map(
                  FailureKey("v1") -> Seq(
                    ValidationFailure(ParentKey(), "v1", "rule1", "msg1")
                  )
                ))
              }
            }
          }
        }
        "when both have a ParentKey" >> {
          "and the parent keys are different" >> {
            "and the keys are different" >> {
              val ruleSet1 = mockFailedRuleSet("v1", "rule1", "msg1", model, ParentKey("parent1"))
              val ruleSet2 = mockFailedRuleSet("v2", "rule1", "msg1", model, ParentKey("parent2"))
              "it should return 2 expected ValidationFailures" >> {
                validator(ruleSet1, ruleSet2)(model) should beFailing(Map(
                  FailureKey("v1", ParentKey("parent1")) -> Seq(
                    ValidationFailure(ParentKey("parent1"), "v1", "rule1", "msg1")
                  ),
                  FailureKey("v2", ParentKey("parent2")) -> Seq(
                    ValidationFailure(ParentKey("parent2"), "v2", "rule1", "msg1")
                  )
                ))
              }
            }
            "and the keys are the same" >> {
              val ruleSet1 = mockFailedRuleSet("v1", "rule1", "msg1", model, ParentKey("parent1"))
              val ruleSet2 = mockFailedRuleSet("v1", "rule1", "msg1", model, ParentKey("parent2"))
              "it should return 2 expected ValidationFailures" >> {
                validator(ruleSet1, ruleSet2)(model) should beFailing(Map(
                  FailureKey("v1", ParentKey("parent1")) -> Seq(
                    ValidationFailure(ParentKey("parent1"), "v1", "rule1", "msg1")
                  ),
                  FailureKey("v1", ParentKey("parent2")) -> Seq(
                    ValidationFailure(ParentKey("parent2"), "v1", "rule1", "msg1")
                  )
                ))
              }
            }
          }
          "and the parent keys are the same" >> {
            "and the keys are different" >> {
              val ruleSet1 = mockFailedRuleSet("v1", "rule1", "msg1", model, ParentKey("parent1"))
              val ruleSet2 = mockFailedRuleSet("v2", "rule1", "msg1", model, ParentKey("parent1"))
              "it should return 2 expected ValidationFailures" >> {
                validator(ruleSet1, ruleSet2)(model) should beFailing(Map(
                  FailureKey("v1", ParentKey("parent1")) -> Seq(
                    ValidationFailure(ParentKey("parent1"), "v1", "rule1", "msg1")
                  ),
                  FailureKey("v2", ParentKey("parent1")) -> Seq(
                    ValidationFailure(ParentKey("parent1"), "v2", "rule1", "msg1")
                  )
                ))
              }
            }
            "and the keys are the same" >> {
              val ruleSet1 = mockFailedRuleSet("v1", "rule1", "msg1", model, ParentKey("parent1"))
              val ruleSet2 = mockFailedRuleSet("v1", "rule2", "msg2", model, ParentKey("parent1"))
              "it should return 1 ValidationFailure with a combination of both ValidationRule failures" >> {
                validator(ruleSet1, ruleSet2)(model) should beFailing(Map(
                  FailureKey("v1", ParentKey("parent1")) -> Seq(
                    ValidationFailure(ParentKey("parent1"), "v1", "rule1", "msg1"),
                    ValidationFailure(ParentKey("parent1"), "v1", "rule2", "msg2")
                  )
                ))
              }
              "but each uses the same ValidationRule" >> {
                val ruleSet1 = mockFailedRuleSet("v1", "rule1", "msg1", model, ParentKey("parent1"))
                val ruleSet2 = mockFailedRuleSet("v1", "rule1", "msg1", model, ParentKey("parent1"))
                "it should return 1 ValidationFailure with out duplicating a ValidationRule failure" >> {
                  validator(ruleSet1, ruleSet2)(model) should beFailing(Map(
                    FailureKey("v1", ParentKey("parent1")) -> Seq(
                      ValidationFailure(ParentKey("parent1"), "v1", "rule1", "msg1")
                    )
                  ))
                }
              }
            }
          }
        }
      }
    }
  }

  private def validator(sets: ValidationRuleSet[TestModel, String]*): Validator[TestModel] = {
    new Validator[TestModel] {
      val ruleSets = sets.toSeq
    }
  }

  private def mockFailedRuleSet(
    key: String,
    ruleKey: String,
    msg: String,
    model: TestModel,
    parentKey: ParentKey = ParentKey()) =
  {
    val ruleSet = mock[ValidationRuleSet[TestModel, String]]
    ruleSet.parentKey returns parentKey
    ruleSet.key returns key
    ruleSet.apply(model) returns Failed(Seq[ValidationFailure](ValidationFailure(parentKey, key, ruleKey, msg)))
    ruleSet
  }

}