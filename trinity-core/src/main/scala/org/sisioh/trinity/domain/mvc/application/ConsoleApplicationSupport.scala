package org.sisioh.trinity.domain.mvc.application

import org.sisioh.trinity.Environment

trait ConsoleApplicationSupport extends Bootstrap {
  this: ConsoleApplication =>

  protected lazy val environment: Environment.Value =
    if (args.size > 0 && args(0) == Environment.Development.toString)
      Environment.Development
    else
      Environment.Product

}
