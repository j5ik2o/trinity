package org.sisioh.trinity.test

import org.sisioh.trinity.application.TrinityApplication
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.domain.controller.{GlobalSettings, Controller}
import org.sisioh.scala.toolbox.LoggingEx

trait ControllerTestSupport extends LoggingEx {

  def buildRequest
  (method: HttpMethod,
   path: String,
   params: Map[String, String] = Map(),
   headers: Map[String, String] = Map())
  (implicit application: TrinityApplication): MockResponse

  def testGet[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.GET, path, params, headers))
  }

  def testPost[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                 (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.POST, path, params, headers))
  }

  def testPut[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.PUT, path, params, headers))
  }

  def testDelete[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                   (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.DELETE, path, params, headers))
  }

  def testHead[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                 (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.HEAD, path, params, headers))
  }

  def testPatch[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                  (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.PATCH, path, params, headers))
  }

  def getController(implicit application: TrinityApplication): Controller

  def getGlobalSettings: Option[GlobalSettings] = None
}
