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
package org.sisioh.trinity.util

import com.twitter.util.{Duration => TDuration}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration => SDuration}

/**
 * Represents the converters to convert between `com.twitter.util.Duration` and `scala.concurrent.duration.Duration`.
 */
object DurationConverters {

  /**
   * Represents the implicit value class to convert from `com.twitter.util.Duration` to  `scala.concurrent.duration.Duration`.
   *
   * @param duration `com.twitter.util.Duration`
   */
  implicit class TDurationToSDuration(val duration: TDuration) extends AnyVal {

    /**
     * Gets a duration as `scala.concurrent.duration.Duration`.
     *
     * @return duration
     */
    def toScala: SDuration = {
      SDuration(duration.inNanoseconds, TimeUnit.NANOSECONDS)
    }

  }

  /**
   * Represents the implicit value class to convert from `scala.concurrent.duration.Duration` to `com.twitter.util.Duration`.
   * @param duration
   */
  implicit class SDurationToTDuration(val duration: SDuration) extends AnyVal {

    /**
     * Gets a duration as `com.twitter.util.Duration`.
     *
     * @return duration
     */
    def toTwitter: TDuration = {
      TDuration(duration.length, duration.unit)
    }

  }

}
