package com.payit.components.core.models

import org.joda.time.DateTime

case class Timestamps(createdAt: DateTime, updatedAt: DateTime)

object Timestamps {
  def apply(now: DateTime = DateTime.now): Timestamps = Timestamps(now, now)
}
