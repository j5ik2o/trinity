package org.sisioh.trinity.domain.config

import com.twitter.util.Eval
import java.io.File
import scala.concurrent.duration.Duration

/**
 * [[org.sisioh.trinity.application.TrinityApplication]]のための設定情報を表す値オブジェクト。
 */
trait Config {

  /**
   * [[org.sisioh.trinity.domain.config.Environment.Value]]
   */
  val environment: Environment.Value

  /**
   * アプリケーション名。
   */
  val applicationName: String

  /**
   * アプリケーションポート。
   */
  val applicationPort: Option[Int]

  /**
   * 統計情報を収集するかどうかのフラグ。
   */
  val statsEnabled: Boolean

  /**
   * 統計情報を返すサーバのポート番号。
   */
  val statsPort: Option[Int]

  /**
   * テンプレートを処理するワーキングディレクトリ。
   */
  val templateWorkDir: File

  /**
   * テンプレートパス。
   */
  val templatePath: String

  /**
   * 開発時にテンプレートなどのリソースを配置するディレクトリ。
   */
  val localDocumentRoot: String

  /**
   * 最大リクエストサイズ
   */
  val maxRequestSize: Option[Int]

  /**
   * 最大レスポンスサイズ
   */
  val maxResponseSize: Option[Int]

  /**
   * 最大コンカレントリクエスト数
   */
  val maxConcurrentRequests: Option[Int]

  /**
   * ホスト接続の最大アイドル時間
   */
  val hostConnectionMaxIdleTime: Option[Duration]

  /**
   * ホスト接続の最大生存時間
   */
  val hostConnectionMaxLifeTime: Option[Duration]

  /**
   * リクエストタイムアウト時間
   */
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
    s"maxResponseSize = $maxResponseSize",
    s"maxConcurrentRequests = $maxConcurrentRequests",
    s"hostConnectionMaxIdleTime = $hostConnectionMaxIdleTime",
    s"hostConnectionMaxLifeTime = $hostConnectionMaxLifeTime",
    s"requestTimeout = $requestTimeout"
  ).mkString("Config(", ",", ")")
}

/**
 * コンパニオンオブジェクト。
 */
object Config {

  import scala.collection.mutable.Map

  private val instances: Map[String, Config] = Map()

  /**
   * 設定ファイルから[[com.twitter.util.Config]]を生成する。
   *
   * @param configFilePath 設定ファイルへのパス
   * @return [[com.twitter.util.Config]]
   */
  def fromFile(configFilePath: String = "trinity.conf"): Config = {
    instances.getOrElse(configFilePath, new Eval().apply[Config](new File(configFilePath)))
  }

  /**
   * ファクトリメソッド。
   *
   * @param environment [[org.sisioh.trinity.domain.config.Environment.Value]]
   * @param applicationName アプリケーション名
   * @param applicationPort アプリケーションポート
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
   * @return
   */
  def apply
  (environment: Environment.Value,
   applicationName: String,
   applicationPort: Option[Int] = None,
   statsEnabled: Boolean = false,
   statsPort: Option[Int] = None,
   templateWorkDir: File = new File("./temp"),
   templatePath: String = "/",
   localDocumentRoot: String = "src/main/resources",
   maxRequestSize: Option[Int] = None,
   maxResponseSize: Option[Int] = None,
   maxConcurrentRequests: Option[Int] = None,
   hostConnectionMaxIdleTime: Option[Duration] = None,
   hostConnectionMaxLifeTime: Option[Duration] = None,
   requestTimeout: Option[Int] = None): Config = new ConfigImpl(
    environment,
    applicationName,
    applicationPort,
    statsEnabled,
    statsPort,
    templateWorkDir,
    templatePath,
    localDocumentRoot,
    maxRequestSize,
    maxResponseSize,
    maxConcurrentRequests,
    hostConnectionMaxIdleTime,
    hostConnectionMaxLifeTime,
    requestTimeout
  )

}

private[config]
case class ConfigImpl
(environment: Environment.Value,
 applicationName: String,
 applicationPort: Option[Int] = None,
 statsEnabled: Boolean = false,
 statsPort: Option[Int] = None,
 templateWorkDir: File = new File("./temp"),
 templatePath: String = "/",
 localDocumentRoot: String = "src/main/resources",
 maxRequestSize: Option[Int] = None,
 maxResponseSize: Option[Int] = None,
 maxConcurrentRequests: Option[Int] = None,
 hostConnectionMaxIdleTime: Option[Duration] = None,
 hostConnectionMaxLifeTime: Option[Duration] = None,
 requestTimeout: Option[Int] = None)
  extends Config


