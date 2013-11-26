package org.sisioh.trinity.infrastructure.util

import scala.util.{Failure, Success, Try}
import java.io.InputStream

private[trinity]
object ResourceUtil {

  def getResourceInputStream(path: String): Try[InputStream] = {
    Try(getClass.getResourceAsStream(path)).flatMap {
      stream =>
        Option(stream).map {
          s =>
            Success(s)
        }.getOrElse(Failure(new Exception))
    }
  }

}
