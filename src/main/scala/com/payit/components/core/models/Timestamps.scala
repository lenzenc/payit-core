package com.payit.components.core.models

import org.joda.time.DateTime

case class Timestamps(createdAt: DateTime = DateTime.now, updatedAt: DateTime = DateTime.now)
