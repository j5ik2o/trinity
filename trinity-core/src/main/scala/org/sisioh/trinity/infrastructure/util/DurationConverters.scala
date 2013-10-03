package org.sisioh.trinity.infrastructure.util

import com.twitter.util.{Duration => TDuration}
import scala.concurrent.duration.{Duration => SDuration, _}
import java.util.concurrent.TimeUnit

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
