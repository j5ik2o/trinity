package org.sisioh.trinity

import java.io.File
import com.twitter.util.Eval
import scala.concurrent.duration.Duration

object Environment extends Enumeration {
  val Product, Development = Value
}

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
}

object Config {

  import scala.collection.mutable.Map

  private val instances: Map[String, Config] = Map()

  def apply(configFilePath: String = "./trinity.conf"): Config = {
    instances.getOrElse(configFilePath, new Eval().apply[Config](new File(configFilePath)))
  }

}
