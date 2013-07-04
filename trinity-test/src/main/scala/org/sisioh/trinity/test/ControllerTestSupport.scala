package org.sisioh.trinity.test

import org.sisioh.trinity.application.TrinityApplication
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.domain.controller.{GlobalSettings, Controller}
import org.sisioh.scala.toolbox.LoggingEx

trait ControllerTestSupport extends LoggingEx {

  def buildRequestByContent
  (method: HttpMethod,
   path: String,
   content: Option[String] = None,
   headers: Map[String, String] = Map())
  (implicit application: TrinityApplication): MockResponse

  def testGetByContent[T](path: String, content: Option[String], headers: Map[String, String] = Map())
                (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByContent(HttpMethod.GET, path, content, headers))
  }

  def testPostByContent[T](path: String, content: Option[String], headers: Map[String, String] = Map())
                 (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByContent(HttpMethod.POST, path, content, headers))
  }

  def testPutByContent[T](path: String, content: Option[String], headers: Map[String, String] = Map())
                (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByContent(HttpMethod.PUT, path, content, headers))
  }

  def testDeleteByContent[T](path: String, content: Option[String], headers: Map[String, String] = Map())
                   (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByContent(HttpMethod.DELETE, path, content, headers))
  }

  def testHeadByContent[T](path: String, content: Option[String], headers: Map[String, String] = Map())
                 (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByContent(HttpMethod.HEAD, path, content, headers))
  }

  def testPatchByContent[T](path: String, content: Option[String], headers: Map[String, String] = Map())
                  (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByContent(HttpMethod.PATCH, path, content, headers))
  }

  def buildRequestByParams
  (method: HttpMethod,
   path: String,
   params: Map[String, String] = Map(),
   headers: Map[String, String] = Map())
  (implicit application: TrinityApplication): MockResponse

  def testGetByParams[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByParams(HttpMethod.GET, path, params, headers))
  }

  def testPostByParams[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                 (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByParams(HttpMethod.POST, path, params, headers))
  }

  def testPutByParams[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByParams(HttpMethod.PUT, path, params, headers))
  }

  def testDeleteByParams[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                   (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByParams(HttpMethod.DELETE, path, params, headers))
  }

  def testHeadByParams[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                 (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByParams(HttpMethod.HEAD, path, params, headers))
  }

  def testPatchByParams[T](path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                  (f: MockResponse => T)(implicit application: TrinityApplication) = {
    f(buildRequestByParams(HttpMethod.PATCH, path, params, headers))
  }

  def getController(implicit application: TrinityApplication): Controller

  def getGlobalSettings: Option[GlobalSettings] = None
}
