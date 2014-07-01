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
package org.sisioh.trinity.domain.mvc.stats

import com.twitter.ostrich.stats.{Stats => SStats}
import org.sisioh.trinity.util.FutureConversation._
import scala.concurrent.{ExecutionContext, Future}

object Stats {

  def timeFutureNanos[A](name: String)(f: => Future[A])
                        (implicit executor: ExecutionContext): Future[A] =
    SStats.timeFutureNanos(name)(f)

  def timeFutureMicro[A](name: String)(f: => Future[A])
                        (implicit executor: ExecutionContext): Future[A] =
    SStats.timeFutureMicros(name)(f)

  def timeFutureMilliso[A](name: String)(f: => Future[A])
                          (implicit executor: ExecutionContext): Future[A] =
    SStats.timeFutureMillisLazy(name)(f)

}