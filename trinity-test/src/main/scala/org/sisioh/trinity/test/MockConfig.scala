package org.sisioh.trinity.test

import java.io.File
import org.sisioh.trinity.domain.config.{Config, Environment}
import scala.concurrent.duration.Duration

/**
 * モック用の[[org.sisioh.trinity.domain.config.Config]]。
 *
 * @param environment
 * @param applicationName
 * @param applicationPort
 * @param statsEnabled
 * @param statsPort
 * @param templateWorkDir
 * @param templatePath
 * @param localDocumentRoot
 * @param maxRequestSize
 * @param maxResponseSize
 * @param maxConcurrentRequests
 * @param hostConnectionMaxIdleTime
 * @param hostConnectionMaxLifeTime
 * @param requestTimeout
 */
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
 requestTimeout: Option[Int] = None)
  extends Config
