package com.payit.components.validation.rules

case class RuleFailure(ruleKey: String, message: String, params: Seq[Any] = Seq.empty)
