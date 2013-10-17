package org.sisioh.trinity.domain.io.http

import scala.language.implicitConversions

import org.jboss.netty.handler.codec.http.HttpMethod

object Method extends Enumeration {

  val Options, Get, Head, Post, Put, Patch, Delete, Trace, Connect = Value

  implicit def toNetty(value: Method.Value): HttpMethod = {
    HttpMethod.valueOf(value.toString.toUpperCase)
  }

  implicit def toTrintiy(value: HttpMethod): Method.Value = {
    val strings = value.toString.splitAt(1)
    val enumName = strings._1.toUpperCase + strings._2.toLowerCase
    Method.withName(enumName)
  }

}
