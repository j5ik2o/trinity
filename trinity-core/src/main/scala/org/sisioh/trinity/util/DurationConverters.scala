package org.sisioh.trinity.util

import com.twitter.util.{Duration => TDuration}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration => SDuration}

object DurationConverters {

  implicit class TDurationToSDuration(val duration: TDuration) extends AnyVal {
    def toScala: SDuration = {
      SDuration(duration.inNanoseconds, TimeUnit.NANOSECONDS)
    }
  }

  implicit class SDurationToTDuration(val duration: SDuration) extends AnyVal {
    def toTwitter: TDuration = {
      TDuration(duration.length, duration.unit)
    }
  }

}
