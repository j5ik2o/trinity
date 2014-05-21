package org.sisioh.trinity.domain.io.http

import java.io.File
import javax.activation.MimetypesFileTypeMap

/**
 * Represents the utility for the mime types.
 */
object MimeTypes {

  private val mimeTypes = new MimetypesFileTypeMap(
    MimeTypes.getClass.getResourceAsStream("/META-INF/mime.types")
  )

  /**
   * Gets the mime type from the file extension.
   *
   * @param fileName file name
   * @return mime type
   */
  def fileExtensionOf(fileName: String): String =
    mimeTypes.getContentType(fileName)

  /**
   * Gets the mime type from [[File]].
   *
   * @param file [[File]]
   * @return mime type
   */
  def fileOf(file: File): String =
    mimeTypes.getContentType(file)

}
