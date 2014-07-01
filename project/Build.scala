import sbt.Keys._
import sbt._
import sbtassembly.Plugin._
import AssemblyKeys._

object TrinityBuild extends Build {

  val myAssemblySettings = mergeStrategy in assembly <<= (mergeStrategy in assembly) {
    (old) => {
      case "rootdoc.txt" | "readme.txt" => MergeStrategy.discard
      case PathList("META-INF", xs@_*) if (xs.length != 0) => {
        xs.last.split("\\.").last.toUpperCase match {
          case "MF" | "SF" | "DSA" | "RSA" => MergeStrategy.discard
          case _ => MergeStrategy.first
        }
      }
      case PathList("com", "twitter", "common", "args", "apt", "cmdline.arg.info.txt.1") => MergeStrategy.first
      case _ => MergeStrategy.first
    }
  }

  val commonSettings = Project.defaultSettings ++ net.virtualvoid.sbt.graph.Plugin.graphSettings ++ Seq(
    organization := "org.sisioh",
    version := "1.0.7",
    scalaVersion := "2.10.3",
    scalacOptions ++= Seq("-encoding", "UTF-8", "-feature", "-deprecation", "-unchecked"),
    javacOptions ++= Seq("-encoding", "UTF-8", "-deprecation"),
    resolvers ++= Seq(
      "Sonatype Snapshot Repository" at "https://oss.sonatype.org/content/repositories/snapshots/",
      "Sonatype Release Repository" at "https://oss.sonatype.org/content/repositories/releases/",
      "Twitter Repository" at "http://maven.twttr.com/",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Seasar Repository" at "http://maven.seasar.org/maven2/"
    ),
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.10.3",
      "junit" % "junit" % "4.8.1" % "test",
      "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
      "org.mockito" % "mockito-core" % "1.9.5" % "test",
      "org.specs2" %% "specs2" % "2.0" % "test",
      "org.seasar.util" % "s2util" % "0.0.1"
    ),
    fork in Test := true,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := {
      _ => false
    },
    publishTo <<= version {
      (v: String) =>
        val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT"))
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomExtra := (
      <url>https://github.com/sisioh/trinity</url>
        <licenses>
          <license>
            <name>Apache License Version 2.0</name>
            <url>http://www.apache.org/licenses/</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:sisioh/trinity.git</url>
          <connection>scm:git:git@github.com:sisioh/trinity.git</connection>
        </scm>
        <developers>
          <developer>
            <id>j5ik2o</id>
            <name>Junichi Kato</name>
            <url>http://j5ik2o.me</url>
          </developer>
        </developers>
      )
  )

  lazy val core = Project(
    id = "trinity-core",
    base = file("trinity-core"),
    settings = commonSettings ++ Seq(
      name := "trinity-core",
      libraryDependencies ++= Seq(
        "org.json4s" %% "json4s-jackson" % "3.2.2",
        "org.sisioh" %% "scala-toolbox" % "0.0.8",
        "org.sisioh" %% "sisioh-config" % "0.0.3",
        "org.slf4j" % "slf4j-api" % "1.6.6",
        "org.slf4j" % "log4j-over-slf4j" % "1.6.6",
        "org.slf4j" % "jul-to-slf4j" % "1.6.6",
        "ch.qos.logback" % "logback-core" % "1.0.7" exclude("org.slf4j", "slf4j-api"),
        "ch.qos.logback" % "logback-classic" % "1.0.7",
        "commons-io" % "commons-io" % "1.3.2",
        "com.twitter" %% "finagle-core" % "6.12.2" excludeAll(
          ExclusionRule(organization = "log4j", name = "log4j"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-api"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-jdk14"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
          ),
        "com.twitter" %% "finagle-http" % "6.12.2" excludeAll(
          ExclusionRule(organization = "log4j", name = "log4j"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-api"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-jdk14"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
          ),
        "com.twitter" %% "finagle-ostrich4" % "6.12.2" excludeAll(
          ExclusionRule(organization = "log4j", name = "log4j"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-api"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-jdk14"),
          ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
          )
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

  /*
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
  */

  lazy val viewScalate = Project(
    id = "trinity-view-scalate",
    base = file("trinity-view-scalate"),
    settings = commonSettings ++ Seq(
      name := "trinity-view-scalate",
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-compiler" % "2.10.2",
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
        "org.specs2" %% "specs2" % "2.0"
      )
    )
  ) dependsOn(core, viewScalate % "test") // , viewThymeleaf % "test", viewVelocity % "test", viewFreeMarker % "test")

  lazy val example = Project(
    id = "trinity-example",
    base = file("trinity-example"),
    settings = commonSettings ++ Seq(
      name := "trinity-example"
    )
  ) dependsOn(core, viewScalate) //, viewThymeleaf, viewVelocity, viewFreeMarker)

  lazy val daemon = Project(
    id = "trinity-daemon",
    base = file("trinity-daemon"),
    settings = commonSettings ++ Seq(
      name := "trinity-daemon",
      libraryDependencies ++= Seq(
        "commons-daemon" % "commons-daemon" % "1.0.15"
      )
    )
  ) dependsOn (core)

  lazy val daemonTest = Project(
    id = "trinity-daemon-test",
    base = file("trinity-daemon-test"),
    settings = commonSettings ++ assemblySettings ++ Seq(
      name := "trinity-daemon-test",
      myAssemblySettings
    )
  ) dependsOn(daemon, test % "test")

  val root = Project(
    id = "trinity",
    base = file("."),
    settings = commonSettings ++ Seq(
      name := "trinity"
    )
  ) aggregate(core, daemon, view, viewScalate, test) //, viewThymeleaf, viewVelocity, viewFreeMarker, test, example)

}
