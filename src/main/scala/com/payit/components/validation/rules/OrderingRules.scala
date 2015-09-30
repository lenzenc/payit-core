package com.payit.components.validation.rules

import com.payit.components.validation.Validated

trait OrderingRules {

  case class GreaterThan[T : Ordering](compareTo: T)(implicit ev: Ordering[ T ]) extends ValidationRule[T] {

    require(compareTo != null, "compareTo value can not be NULL")

    def apply(value: T): Validated[RuleFailure, T] = {
      if (value != null && ev.gt(value, compareTo)) succeeded(value) else failed(
        "greaterthan",
        s"should be greater than $compareTo",
        Seq(compareTo))
    }

  }

  case class GreaterThanOrEqual[T : Ordering](compareTo: T)(implicit ev: Ordering[ T ]) extends ValidationRule[T] {

    require(compareTo != null, "compareTo value can not be NULL")

    def apply(value: T): Validated[RuleFailure, T] = {
      if (value != null && ev.gteq(value, compareTo)) succeeded(value) else failed(
        "greaterthanorequal",
        s"should be greater than or equal to $compareTo",
        Seq(compareTo))
    }

  }

  case class LessThan[T : Ordering](compareTo: T)(implicit ev: Ordering[ T ]) extends ValidationRule[T] {

    require(compareTo != null, "compareTo value can not be NULL")

    def apply(value: T): Validated[RuleFailure, T] = {
      if (value != null && ev.lt(value, compareTo)) succeeded(value) else failed(
        "lessthan",
        s"should be less than $compareTo",
        Seq(compareTo))
    }

  }

  case class LessThanOrEqual[T : Ordering](compareTo: T)(implicit ev: Ordering[ T ]) extends ValidationRule[T] {

    require(compareTo != null, "compareTo value can not be NULL")

    def apply(value: T): Validated[RuleFailure, T] = {
      if (value != null && ev.lteq(value, compareTo)) succeeded(value) else failed(
        "lessthanorequal",
        s"should be less than or equal to $compareTo",
        Seq(compareTo))
    }

  }

  case class Between[T : Ordering](lower: T, upper: T)(implicit ev: Ordering[ T ]) extends ValidationRule[T] {

    require(lower != null, "lower compareTo value can not be NULL")
    require(upper != null, "upper compareTo value can not be NULL")

    def apply(value: T): Validated[RuleFailure, T] = {
      if (value != null && ev.gteq(value, lower) && ev.lteq(value, upper)) succeeded(value) else failed(
        "between",
        s"should be between $lower and $upper",
        Seq(lower, upper))
    }

  }

}
