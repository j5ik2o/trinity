package org.sisioh.trinity

object ConfigProvider {

  import com.typesafe.config._
  import com.github.kxbmap.configs._

  val config = ConfigFactory.load("trinity")

  def get(key: String): String = {
    config.get[String](key)
  }

  def getInt(key: String): Int = {
    config.get[Int](key)
  }

  def getBool(key: String): Boolean = {
    config.get[Boolean](key)
  }

}
