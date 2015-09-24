package com.payit.components.mongo.migrations

sealed trait MigrationCommand
case object ApplyMigrations extends MigrationCommand
case object ResetApplyMigrations extends MigrationCommand
case class RollbackMigration(versions: Int = 1) extends MigrationCommand
