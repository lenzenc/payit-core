package com.payit.components.core.models

import com.payit.components.validation.{ValidationFailure, FailureKey}

class ModelValidationException(failureMap: Map[FailureKey, Seq[ValidationFailure]]) extends RuntimeException
