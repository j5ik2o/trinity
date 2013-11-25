package org.sisioh.trinity.domain.mvc.resource

import java.io.{FileInputStream, File, InputStream}
import org.sisioh.trinity.domain.mvc.Environment
import scala.util.{Failure, Success, Try}
import org.sisioh.trinity.infrastructure.util.ResourceUtil

/**
 * ファイルリソースを解決するためのクラス。
 *
 * @param environment [[org.sisioh.trinity.domain.mvc.Environment]]
 * @param localBasePath Development時のローカルベースパス
 */
case class FileResourceResolver(environment: Environment.Value, localBasePath: File) {

  def hasFile(path: String): Boolean = {
    if (environment == Environment.Product) {
      hasResourceFile(path)
    } else {
      hasLocalFile(path)
    }
  }

  def getInputStream(path: String): Try[InputStream] = {
    if (environment == Environment.Product) {
      ResourceUtil.getResourceInputStream(path)
    } else {
      getLocalInputStream(path)
    }
  }

  private def getLocalInputStream(path: String): Try[InputStream] = Try {
    val file = new File(localBasePath, path)
    new FileInputStream(file)
  }

  private def hasResourceFile(path: String): Boolean = {
    ResourceUtil.getResourceInputStream(path).map {
      _ =>
        true
    }.getOrElse(false)
  }

  private def hasLocalFile(path: String): Boolean = {
    val file = new File(localBasePath, path)
    if (file.toString.contains("trinity-core/src/test")) false
    else if (!file.exists || file.isDirectory) false
    else if (!file.canRead) false
    else true
  }


}
