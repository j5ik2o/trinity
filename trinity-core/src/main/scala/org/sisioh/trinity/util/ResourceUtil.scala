package org.sisioh.trinity.util

import java.io.InputStream
import scala.util.{Failure, Success, Try}

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
