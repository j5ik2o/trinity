package org.sisioh.trinity.domain.resource

import org.sisioh.trinity.domain.config.{Environment, Config}
import java.io.{FileInputStream, File, InputStream}

object FileResolver {

  def apply(config: Config) =
    new FileResolver(config)

}

class FileResolver(config: Config) {

  def hasFile(path: String): Boolean = {
    if (config.environment == Environment.Product) {
      hasResourceFile(path)
    } else {
      hasLocalFile(path)
    }
  }

  def getInputStream(path: String): InputStream = {
    if (config.environment == Environment.Product) {
      getResourceInputStream(path)
    } else {
      getLocalInputStream(path)
    }
  }

  private def getResourceInputStream(path: String): InputStream = {
    getClass.getResourceAsStream(path)
  }

  private def getLocalInputStream(path: String): InputStream = {
    val file = new File(config.localDocumentRoot, path)
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
    val file = new File(config.localDocumentRoot, path)
    if (file.toString.contains("trinity-core/src/test")) false
    else if (!file.exists || file.isDirectory) false
    else if (!file.canRead) false
    else true
  }

}
