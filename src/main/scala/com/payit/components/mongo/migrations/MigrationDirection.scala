package com.payit.components.mongo.migrations

sealed trait MigrationDirection
case object Up extends MigrationDirection
case object Down extends MigrationDirection
