package com.payit.components.validation

case class FailureKey(key: String, parentKey: ParentKey = ParentKey())

trait Validator[T] extends (T => Validated[Map[FailureKey, Seq[ValidationFailure]], T]) {

  def parentKey: ParentKey = ParentKey()
  def ruleSets: Seq[ValidationRuleSet[T, _]]

  def apply(obj: T): Validated[Map[FailureKey, Seq[ValidationFailure]], T] = {
    var failures = Map[FailureKey, Seq[ValidationFailure]]()
    ruleSets.foreach { ruleSet =>
      ruleSet(obj) match {
        case Success(_) => Nil
        case Failed(f) => {
          failures = failures + (
            FailureKey(ruleSet.key, ruleSet.parentKey) ->
              concat(failures.getOrElse(FailureKey(ruleSet.key, ruleSet.parentKey), Seq.empty[ValidationFailure]), f)
            )
        }
      }
    }

    if (failures.isEmpty) Success(obj) else Failed(failures)
  }

  private def concat(v1: Seq[ValidationFailure], v2: Seq[ValidationFailure]): Seq[ValidationFailure] = {
    val ruleKeys = v1.map{ f => f.ruleKey}
    v1 ++ v2.filter { f => !ruleKeys.contains(f.ruleKey) }
  }

}