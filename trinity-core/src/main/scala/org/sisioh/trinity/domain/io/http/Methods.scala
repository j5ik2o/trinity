package org.sisioh.trinity.domain.io.http

import org.jboss.netty.handler.codec.http.HttpMethod
import scala.language.implicitConversions


object Methods extends Enumeration {

  val Options, Get, Head, Post, Put, Patch, Delete, Trace, Connect = Value

  private[domain] implicit def toNetty(value: Methods.Value): HttpMethod = {
    HttpMethod.valueOf(value.toString.toUpperCase)
  }

  private[domain] implicit def toTrintiy(value: HttpMethod): Methods.Value = {
    val strings = value.toString.splitAt(1)
    val enumName = strings._1.toUpperCase + strings._2.toLowerCase
    Methods.withName(enumName)
  }

}
