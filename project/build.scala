import sbt._
import sbt.Keys._
import spray.revolver.RevolverPlugin._

object PayitCore extends Build {

  lazy val _scalacOptions = Seq("-deprecation", "-unchecked", "-feature")

  val testAll = TaskKey[Unit]("test-all", "Runs All Unit & Integration Tests")

  val testAllTask = testAll := ()

  lazy val commonSettings = Seq(
    version := "1.0",
    organization := "com.payit",
    scalaVersion := "2.11.4",
    ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
    resolvers ++= Seq(
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases",
      "Sonatype Releases"  at "http://oss.sonatype.org/content/repositories/releases",
      "spray repo" at "http://repo.spray.io/",
      "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
    ),
    scalacOptions ++= _scalacOptions,
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature"),
    scalacOptions in Test ++= Seq("-Yrangepos"),
    scalacOptions in (Compile, console) ++= Seq("-Xprint:types"),
    javacOptions ++= Seq("-Xog-implicit-conversions"),
    parallelExecution in IntegrationTest := false
  )

  lazy val root = Project(id = "payit-core", base = file("."),
    configurations = Seq(
      IntegrationTest
    ),
    settings = commonSettings ++ Seq(
      name := "payit-core",
      libraryDependencies ++= Seq(
        "org.specs2" %% "specs2-core" % "3.6.4" % "it,test",
        "joda-time" % "joda-time" % "2.8.2" % "compile",
        "org.joda" % "joda-convert" % "1.7" % "compile",
        "com.typesafe" % "config" % "1.3.0" % "compile",
        "ch.qos.logback" % "logback-classic" % "1.1.3" % "compile",
        "com.typesafe.akka" %% "akka-slf4j" % "2.3.6" % "compile",
        "io.spray" %% "spray-can" % "1.3.3" % "compile",
        "io.spray" %% "spray-routing" % "1.3.3" % "compile",
        "io.spray" %% "spray-testkit" % "1.3.3" % "compile",
        "io.spray"%%"spray-json"%"1.3.2" % "compile",
        "org.mongodb" % "casbah_2.11" % "2.8.2" % "compile",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0" % "compile"
      ),
      testAllTask
    ) ++ Revolver.settings ++ Defaults.itSettings ++ Seq(
      testAll <<= testAll.dependsOn(test in IntegrationTest),
      testAll <<= testAll.dependsOn(test in Test)
    )
  )

}