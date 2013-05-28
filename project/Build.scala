import sbt._
import sbt.Keys._

object TrinityBuild extends Build {

  def publish = publishTo <<= (version) {
    version: String =>
      if (version.trim.endsWith("SNAPSHOT")) {
        Some(Resolver.file("snaphost", new File("./repos/snapshot")))
      } else {
        Some(Resolver.file("release", new File("./repos/release")))
      }
  }

  lazy val root = Project(
    id = "finatra",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "trinity",
      organization := "org.sisioh",
      version := "0.0.1-SNAPSHOT",
      scalaVersion := "2.10.1",
      resolvers ++= Seq(
        "Twitter Repository" at "http://maven.twttr.com/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "Sisioh DDD Maven Release Repository" at "http://sisioh.github.com/scala-dddbase/repos/release/",
        "Sisioh DDD Maven Snapshot Repository" at "http://sisioh.github.com/scala-dddbase/repos/snapshot/",
        "Sisioh BaseUnits Mave Release Repository" at "http://sisioh.github.com/baseunits-scala/repos/release/",
        "Sisioh Scala Toolbox Release Repository" at "http://sisioh.github.io/scala-toolbox/repos/release/"
      ),
      libraryDependencies ++= Seq(
        "junit" % "junit" % "4.8.1" % "test",
        "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
        "org.specs2" %% "specs2" % "1.14" % "test",
        "org.mockito" % "mockito-core" % "1.9.5" % "test",
        "org.json4s" %% "json4s-jackson" % "3.2.2",
        "org.sisioh" %% "scala-dddbase-core" % "0.1.2",
        "org.clapper" %% "grizzled-slf4j" % "1.0.1",
        "org.sisioh" %% "scala-toolbox" % "0.0.4",
        "org.scala-lang" % "scala-reflect" % "2.10.1",
        "org.slf4j" % "slf4j-api" % "1.6.6",
        "org.slf4j" % "log4j-over-slf4j" % "1.6.6",
        "org.slf4j" % "jul-to-slf4j" % "1.6.6",
        "ch.qos.logback" % "logback-core" % "1.0.7" exclude("org.slf4j", "slf4j-api"),
        "ch.qos.logback" % "logback-classic" % "1.0.7",
        "commons-daemon" % "commons-daemon" % "1.0.15",
        "com.twitter" %% "finagle-core" % "6.4.0" excludeAll(
          ExclusionRule(organization = "log4j", name = "log4j"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-api"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-jdk14"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
          ),
        "com.twitter" %% "finagle-http" % "6.4.0" excludeAll(
          ExclusionRule(organization = "log4j", name = "log4j"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-api"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-jdk14"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
          ),
        "com.twitter" %% "util-eval" % "6.3.4" excludeAll(
          ExclusionRule(organization = "log4j", name = "log4j"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-api"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-jdk14"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
          ),
        "commons-io" % "commons-io" % "1.3.2",
        "com.github.spullara.mustache.java" % "compiler" % "0.8.8"
      ),
      publish
    )
  )
}
