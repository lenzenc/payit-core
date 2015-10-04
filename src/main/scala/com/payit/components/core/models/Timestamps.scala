package com.payit.components.core.models

import org.joda.time.DateTime

case class Timestamps(createdAt: DateTime, updatedAt: DateTime) {

  def withUpdatedAt(now: DateTime = DateTime.now) = copy(updatedAt = now)

}

object Timestamps {
  def apply(now: DateTime = DateTime.now): Timestamps = Timestamps(now, now)
}
