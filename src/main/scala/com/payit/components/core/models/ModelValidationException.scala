package com.payit.components.core.models

import com.payit.validations.ValidationFailure

class ModelValidationException(failureMap: Map[String, Seq[ValidationFailure]]) extends RuntimeException
