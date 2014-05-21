/*
 * Copyright 2013 Sisioh Project and others. (http://sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.trinity.domain.mvc.resource

import java.io.{FileInputStream, File, InputStream}
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.mvc.Environment
import org.sisioh.trinity.util.ResourceUtil
import scala.util.Try

/**
 * Represents the class to resolve file's resources.
 *
 * @param environment [[org.sisioh.trinity.domain.mvc.Environment]]
 * @param localBasePath local base path when development mode
 */
case class FileResourceResolver(environment: Environment.Value, localBasePath: File)
  extends LoggingEx {

  /**
   * Gets whether exist the file that be specified by the file path.
   *
   * @param path path to file
   * @return Returns true if it exists.
   */
  def hasFile(path: String): Boolean = {
    if (environment == Environment.Product) {
      hasResourceFile(path)
    } else {
      hasLocalFile(path)
    }
  }

  /**
   * Gets [[InputStream]] that be specified by the file path.
   *
   * @param path path to file
   * @return wrapped [[InputStream]] around `scala.util.Try`
   */
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
      fh => fh.close(); true
    }.getOrElse(false)
  }

  private def hasLocalFile(path: String): Boolean = {
    val file = new File(localBasePath, path)
    val result = if (file.toString.contains("trinity-core/src/test")) false
    else if (!file.exists || file.isDirectory) false
    else if (!file.canRead) false
    else true
    debug(s"hasLocalFile($path) = $result")
    result
  }

}
