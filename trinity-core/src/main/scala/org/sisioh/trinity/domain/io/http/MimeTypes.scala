package org.sisioh.trinity.domain.io.http

import javax.activation.MimetypesFileTypeMap
import java.io.File

object MimeTypes {

  private val mimeTypes = new MimetypesFileTypeMap(MimeTypes.getClass.getResourceAsStream("/META-INF/mime.types"))

  def fileExtensionOf(fileName: String) = {
    mimeTypes.getContentType(fileName)
  }

  def fileOf(file: File) =
    mimeTypes.getContentType(file)

}
