package com.payit.components.mongo.migrations

import java.io.File
import java.net.{URLDecoder, URL}

import com.mongodb.casbah.Imports._
import com.payit.components.core.Configuration
import com.typesafe.scalalogging.LazyLogging
import org.clapper.classutil.ClassFinder
import org.joda.time.DateTime

import scala.collection.immutable.TreeMap
import scala.collection.{immutable, mutable}

class MongoMigrator(mongoConfig: String, config: Configuration) extends LazyLogging {

  com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers()

  lazy val client: MongoClient = MongoClient(
    config.getString(s"mongo.$mongoConfig.host").getOrElse("localhost"),
    config.getInt(s"mongo.$mongoConfig.port").getOrElse(27017))
  val dbName: String = config.getString(s"mongo.$mongoConfig.dbname").getOrElse(
    sys.error(s"Unable to find property: 'mongo.$mongoConfig.dbname'")
  )
  val migrationCollectionName: String = "schema_migrations"

  def migrate(command: MigrationCommand, basePackage: String) = {

    val db = client(dbName)
    client.setWriteConcern(WriteConcern.Safe)

    val migrations: immutable.SortedMap[Long, Class[_ <: MongoMigration]] = findMigrations(basePackage)
    val versions: Vector[Long] = migrations.keySet.toVector
    val versionCollection = db(migrationCollectionName)
    var appliedVersions = getAppliedVersions(versionCollection)

    case class ApplyAndRemove(applyVersions: Vector[Long], removeVersions: Vector[Long])

    val applyRemove = command match {
      case ApplyMigrations => ApplyAndRemove(versions, Vector.empty[Long])
      case ResetApplyMigrations =>
        client.dropDatabase(dbName)
        versionCollection.createIndex(
          MongoDBObject("version" -> 1),
          MongoDBObject("unique" -> true, "name" -> "UNIQ_VERSION_IDX"))
        appliedVersions = Vector.empty[Long]
        ApplyAndRemove(versions, Vector.empty[Long])
      case RollbackMigration(count) =>
        ApplyAndRemove(
          Vector.empty[Long],
          appliedVersions.reverse.take(if (count > appliedVersions.length) appliedVersions.length else count))
    }

    applyRemove.removeVersions.foreach { version =>
      migrations.get(version) match {
        case Some(clazz) => runMigration(clazz, Down, version, versionCollection, db)
        case None => sys.error(s"Database has migration version: $version but here is not migration class available " +
          "for that version")
      }
    }

    applyRemove.applyVersions.foreach { version =>
      if (!appliedVersions.contains(version)) {
        migrations.get(version) match {
          case Some(clazz) => runMigration(clazz, Up, version, versionCollection, db)
          case None => sys.error(s"Trying to migration to version: $version but migration class does not exist.")
        }
      }
    }

  }

  private def getAppliedVersions(versionCollection: MongoCollection): Vector[Long] = {
    versionCollection.find(
      MongoDBObject(),
      MongoDBObject("version" -> 1, "_id" -> 0)).
      sort(MongoDBObject("version" -> 1)).flatMap(_.getAs[Long]("version")).toVector
  }

  private def runMigration(
                            migrationClass: Class[_ <: MongoMigration],
                            direction: MigrationDirection,
                            version: Long,
                            versionCollection: MongoCollection,
                            db: MongoDB) =
  {

    val migration = migrationClass.getConstructor(classOf[MongoDB]).newInstance(db)
    logger.debug(s"Running Migration: ${migrationClass.getName}")
    direction match {
      case Up =>
        migration.up
        versionCollection.insert(MongoDBObject("version" -> version, "appliedAt" -> DateTime.now))
      case Down =>
        migration.down
        versionCollection.remove(MongoDBObject("version" -> version))
    }

  }

  private def findMigrations(packageName: String): immutable.SortedMap[Long, Class[_ <: MongoMigration]] = {

    var results = new TreeMap[Long, Class[_ <: MongoMigration]]
    val path = packageName.replace('.','/')
    val urls = this.getClass.getClassLoader.getResources(path)
    val classNames = new mutable.HashSet[String]
    while (urls.hasMoreElements) {
      classNames ++= classNamesInResources(urls.nextElement(), packageName)
    }

    var seenVersions = new immutable.TreeMap[Long, String]
    val seenDescriptions = new mutable.HashMap[String, String]
    val reStr = """Migrate_(\d+)_([_a-zA-Z0-9]*)"""
    val re = java.util.regex.Pattern.compile(reStr)

    classNames.foreach { name =>
      val index = name.lastIndexOf('.')
      val baseName = if (index == -1) name else name.substring(index + 1)
      val matcher = re.matcher(baseName)
      if (matcher.matches) {
        val versionStr = matcher.group(1)
        val versionOpt = try Some(versionStr.toLong) catch { case e: NumberFormatException =>
          println(s"Skipping Migration because the version of define class: $name is not a valid number")
          None
        }

        versionOpt match {
          case Some(version) =>
            seenVersions.get(version) match {
              case Some(cn) => sys.error(s"$name migration defines a duplicate version number of $cn")
              case None => seenVersions = seenVersions.insert(version, name)
            }

            val description = matcher.group(2)
            seenDescriptions.get(description) match {
              case Some(cn) => sys.error(s"$name defines a duplicate description for $cn")
              case None => seenDescriptions.put(description, name)
            }
          case None =>
        }
      } else {
        println(s"Skipping Migration because name of class: $name does not match patter Migrate_<<version#>>_")
      }
    }

    seenVersions.foreach { case(version, name) =>

      var c: Class[_] = null
      try {
        c = Class.forName(name)
        val castedClass = c.asSubclass(classOf[MongoMigration])
        results = results.insert(version, castedClass)
      }
      catch { case e: Exception => }

    }

    results

  }

  private def classNamesInResources(url: URL, packageName: String): mutable.Set[String] = {

    val classNames = new mutable.HashSet[String]
    val u = URLDecoder.decode(url.toString, "UTF-8")

    if (u.startsWith("file:")) {

      val file = new File(u.substring("file:".length))
      val classesMap = ClassFinder.classInfoMap(ClassFinder(
        Seq(new File(u.substring("file:".length)))).getClasses().toIterator).foreach {
        case(clazz, info) if (info.superClassName.equals(classOf[MongoMigration].getName)) => classNames += clazz
      }

    }
    classNames

  }

}
