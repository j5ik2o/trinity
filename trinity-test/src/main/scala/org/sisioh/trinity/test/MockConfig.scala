package org.sisioh.trinity.test

import org.sisioh.trinity.domain.config.{Config, Environment}
import java.io.File
import scala.concurrent.duration.Duration

case class MockConfig
(environment: Environment.Value = Environment.Development,
 applicationName: String = "TestApplication",
 applicationPort: Option[Int] = None,
 statsEnabled: Boolean = false,
 statsPort: Option[Int] = None,
 templateWorkDir: File = new File("./temp"),
 templatePath: String = "/",
 localDocumentRoot: String = "src/test/resources",
 maxRequestSize: Option[Int] = None,
 maxResponseSize: Option[Int] = None,
 maxConcurrentRequests: Option[Int] = None,
 hostConnectionMaxIdleTime: Option[Duration] = None,
 hostConnectionMaxLifeTime: Option[Duration] = None,
 requestTimeout: Option[Int] = None) extends Config
