package org.sisioh.trinity.domain.config

import com.twitter.util.Eval
import java.io.File
import scala.concurrent.duration.Duration


trait Config {

  val environment: Environment.Value

  val applicationName: String
  val applicationPort: Option[Int]

  val statsEnabled: Boolean
  val statsPort: Option[Int]

  val templateWorkDir: File
  val templatePath: String

  val localDocumentRoot: String

  val maxRequestSize: Option[Int]
  val maxResponseSize: Option[Int]

  val maxConcurrentRequests: Option[Int]
  val hostConnectionMaxIdleTime: Option[Duration]
  val hostConnectionMaxLifeTime: Option[Duration]
  val requestTimeout: Option[Int]

  override def toString = Seq(
    s"environment = $environment",
    s"applicationName = $applicationName",
    s"applicationPort = $applicationPort",
    s"statsEnabled = $statsEnabled",
    s"statsPort = $statsPort",
    s"templateWorkDir = $templateWorkDir",
    s"templatePath = $templatePath",
    s"localDocumentRoot = $localDocumentRoot",
    s"maxRequestSize = $maxRequestSize",
    maxResponseSize,
    maxConcurrentRequests,
    hostConnectionMaxIdleTime,
    hostConnectionMaxLifeTime,
    requestTimeout
  ).mkString("Config(", ",", ")")
}

object Config {

  import scala.collection.mutable.Map

  private val instances: Map[String, Config] = Map()

  def apply(configFilePath: String = "trinity.conf"): Config = {
    instances.getOrElse(configFilePath, new Eval().apply[Config](new File(configFilePath)))
  }

}
