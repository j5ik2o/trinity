import sbt.Keys._
import sbt._

object TrinityBuild extends Build {

  def publish = publishTo <<= (version) {
    version: String =>
      if (version.trim.endsWith("SNAPSHOT")) {
        Some(Resolver.file("snaphost", new File("./repos/snapshot")))
      } else {
        Some(Resolver.file("release", new File("./repos/release")))
      }
  }

  val commonSettings = Project.defaultSettings ++ Seq(
    organization := "org.sisioh",
    version := "0.0.6",
    scalaVersion := "2.10.2",
    scalacOptions ++= Seq("-encoding", "UTF-8", "-feature", "-deprecation", "-unchecked"),
    javacOptions ++= Seq("-encoding", "UTF-8", "-deprecation"),
    resolvers ++= Seq(
      "Twitter Repository" at "http://maven.twttr.com/",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Sisioh DDD Maven Release Repository" at "http://sisioh.github.com/scala-dddbase/repos/release/",
      "Sisioh DDD Maven Snapshot Repository" at "http://sisioh.github.com/scala-dddbase/repos/snapshot/",
      "Sisioh BaseUnits Mave Release Repository" at "http://sisioh.github.com/baseunits-scala/repos/release/",
      "Sisioh Scala Toolbox Release Repository" at "http://sisioh.github.io/scala-toolbox/repos/release/",
      "Seasar Repository" at "http://maven.seasar.org/maven2/"

    ),
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.10.2",
      "junit" % "junit" % "4.8.1" % "test",
      "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
      "org.mockito" % "mockito-core" % "1.9.5" % "test",
      "org.specs2" %% "specs2" % "1.14" % "test",
      "org.seasar.util" % "s2util" % "0.0.1"
    ),
    publish
  )

  lazy val core = Project(
    id = "trinity-core",
    base = file("trinity-core"),
    settings = commonSettings ++ Seq(
      name := "trinity-core",
      libraryDependencies ++= Seq(
        "org.json4s" %% "json4s-jackson" % "3.2.2",
        "org.sisioh" %% "scala-dddbase-core" % "0.1.13",
        "org.sisioh" %% "scala-toolbox" % "0.0.6",
        "org.slf4j" % "slf4j-api" % "1.6.6",
        "org.slf4j" % "log4j-over-slf4j" % "1.6.6",
        "org.slf4j" % "jul-to-slf4j" % "1.6.6",
        "ch.qos.logback" % "logback-core" % "1.0.7" exclude("org.slf4j", "slf4j-api"),
        "ch.qos.logback" % "logback-classic" % "1.0.7",
        "commons-daemon" % "commons-daemon" % "1.0.15",
        "com.github.kxbmap" %% "configs" % "0.1.0",
        "com.twitter" %% "finagle-core" % "6.4.1" excludeAll(
          ExclusionRule(organization = "log4j", name = "log4j"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-api"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-jdk14"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
          ),
        "com.twitter" %% "finagle-http" % "6.4.1" excludeAll(
          ExclusionRule(organization = "log4j", name = "log4j"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-api"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-jdk14"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
          ),
        "com.twitter" %% "finagle-ostrich4" % "6.4.1" excludeAll(
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
        "commons-io" % "commons-io" % "1.3.2"
      )
    )
  )

  lazy val view = Project(
    id = "trinity-view",
    base = file("trinity-view"),
    settings = commonSettings ++ Seq(
      name := "trinity-view"
    )
  ) dependsOn (core)

  lazy val viewFreeMarker = Project(
    id = "trinity-view-freemarker",
    base = file("trinity-view-freemarker"),
    settings = commonSettings ++ Seq(
      name := "trinity-view-freemarker",
      libraryDependencies ++= Seq(
        "org.freemarker" % "freemarker" % "2.3.19"
      )
    )
  ) dependsOn (view)

  lazy val viewVelocity = Project(
    id = "trinity-view-velocity",
    base = file("trinity-view-velocity"),
    settings = commonSettings ++ Seq(
      name := "trinity-view-velocity",
      libraryDependencies ++= Seq(
        "velocity" % "velocity" % "1.5"
      )
    )
  ) dependsOn (view)

  lazy val viewThymeleaf = Project(
    id = "trinity-view-thymeleaf",
    base = file("trinity-view-thymeleaf"),
    settings = commonSettings ++ Seq(
      name := "trinity-view-thymeleaf",
      libraryDependencies ++= Seq(
        "org.thymeleaf" % "thymeleaf" % "2.0.17"
      )
    )
  ) dependsOn (view)

  lazy val viewScalate = Project(
    id = "trinity-view-scalate",
    base = file("trinity-view-scalate"),
    settings = commonSettings ++ Seq(
      name := "trinity-view-scalate",
      libraryDependencies ++= Seq(
        "org.fusesource.scalate" %% "scalate-core" % "1.6.1"
      )
    )
  ) dependsOn (view)

  lazy val test = Project(
    id = "trinity-test",
    base = file("trinity-test"),
    settings = commonSettings ++ Seq(
      name := "trinity-test",
      libraryDependencies ++= Seq(
        "org.hamcrest" % "hamcrest-all" % "1.3",
        "org.mockito" % "mockito-core" % "1.9.5",
        "org.specs2" %% "specs2" % "1.14"
      )
    )
  ) dependsOn(core, viewScalate % "test", viewThymeleaf % "test", viewVelocity % "test", viewFreeMarker % "test")


  lazy val example = Project(
    id = "trinity-example",
    base = file("trinity-example"),
    settings = commonSettings ++ Seq(
      name := "trinity-example"
    )
  ) dependsOn(core, viewScalate, viewThymeleaf, viewVelocity, viewFreeMarker)

  val root = Project(
    id = "trinity",
    base = file("."),
    settings = commonSettings ++ Seq(
      name := "trinity"
    )
  ) aggregate(core, view, viewScalate, viewThymeleaf, viewVelocity, viewFreeMarker, test, example)

}
