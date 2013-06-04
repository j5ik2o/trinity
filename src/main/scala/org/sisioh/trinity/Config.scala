package org.sisioh.trinity

object Config {

  val defaults = Map(
    "env" -> "development",
    "port" -> "7070",
    "name" -> "finatra",
    "pid_enabled" -> "false",
    "pid_path" -> "finatra.pid",
    "log_path" -> "logs/finatra.log",
    "log_node" -> "finatra",
    "stats_enabled" -> "true",
    "stats_port" -> "9990",
    "template_path" -> "/",
    "local_docroot" -> "src/main/resources",
    "max_request_megabytes" -> "5"
  )

  def get(key:String):String = {
    Option(System.getProperty(key)) match {
      case Some(prop) => prop
      case None => defaults.get(key).get
    }
  }

  def getInt(key:String):Int = {
    augmentString(get(key)).toInt
  }

  def getBool(key:String):Boolean = {
    get(key) == "true" || get(key) == "1"
  }

  def printConfig() {
    defaults.foreach { xs =>
      println("-D" + xs._1 + "=" + Config.get(xs._1) + "\\")
    }
  }

}
