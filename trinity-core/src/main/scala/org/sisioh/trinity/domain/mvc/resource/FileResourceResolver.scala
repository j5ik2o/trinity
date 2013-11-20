package org.sisioh.trinity.domain.mvc.resource

import java.io.{FileNotFoundException, FileInputStream, File, InputStream}
import org.sisioh.trinity.domain.mvc.Environment
import scala.util.{Failure, Success, Try}

case class FileResourceResolver(environment: Environment.Value, localPath: File) {

  def hasFile(path: String): Boolean = {
    if (environment == Environment.Product) {
      hasResourceFile(path)
    } else {
      hasLocalFile(path)
    }
  }

  def getInputStream(path: String): Try[InputStream] = {
    if (environment == Environment.Product) {
      getResourceInputStream(path)
    } else {
      getLocalInputStream(path)
    }
  }

  private def getResourceInputStream(path: String): Try[InputStream] = {
    Option(getClass.getResourceAsStream(path)).map(s => Success(s)).getOrElse(Failure(new FileNotFoundException(path)))
  }

  private def getLocalInputStream(path: String): Try[InputStream] = Try {
    val file = new File(localPath, path)
    new FileInputStream(file)
  }

  private def hasResourceFile(path: String): Boolean = {
    val fi = getClass.getResourceAsStream(path)
    try {
      if (fi != null && fi.available > 0) {
        true
      } else {
        false
      }
    } catch {
      case e: Exception =>
        false
    }
  }

  private def hasLocalFile(path: String): Boolean = {
    val file = new File(localPath, path)
    if (file.toString.contains("trinity-core/src/test")) false
    else if (!file.exists || file.isDirectory) false
    else if (!file.canRead) false
    else true
  }


}
