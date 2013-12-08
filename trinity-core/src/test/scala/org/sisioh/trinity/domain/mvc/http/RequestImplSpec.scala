package org.sisioh.trinity.domain.mvc.http

import org.junit.runner.RunWith
import org.sisioh.trinity.domain.io.http.{HeaderName, Cookie, ProtocolVersion, Methods}
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import scala.concurrent.Future
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

@RunWith(classOf[JUnitRunner])
class RequestImplSpec extends Specification {

  "request" should {
    "has action as some" in {
      val method = Methods.Get
      val uri = "/"
      val headers = Seq.empty[(HeaderName, Any)]
      val cookies = Seq.empty[Cookie]
      val attributes = Map.empty[String, Any]
      val content = ChannelBuffer.empty
      val action = Some(new SimpleAction {
        def apply(request: Request): Future[Response] = Future.successful(Response())
      })
      val routeParams = Map.empty[String, String]
      val globalSettings = None
      val error = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, headers, cookies, attributes, content, action, routeParams, globalSettings, error, false, version)
      request.method must_== method
      request.uri must_== uri
      request.action must_== action
      request.routeParams must_== routeParams
      request.globalSettings must_== globalSettings
      request.error must_== error
      request.protocolVersion must_== version
    }
    "execute action" in {
      val method = Methods.Get
      val uri = "/"
      val headers = Seq.empty[(HeaderName, Any)]
      val cookies = Seq.empty[Cookie]
      val attributes = Map.empty[String, Any]
      val content = ChannelBuffer.empty
      val responseFuture = Future.successful(Response())
      val action = new SimpleAction {
        def apply(request: Request): Future[Response] = responseFuture
      }
      val routeParams = Map.empty[String, String]
      val globalSettings = None
      val error = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, headers, cookies, attributes, content, Some(action), routeParams, globalSettings, error, false, version)
      request.execute(action) must_== responseFuture
    }
    "be changed action" in {
      val method = Methods.Get
      val uri = "/"
      val headers = Seq.empty[(HeaderName, Any)]
      val cookies = Seq.empty[Cookie]
      val attributes = Map.empty[String, Any]
      val content = ChannelBuffer.empty
      val responseFuture = Future.successful(Response())
      val action = Some(new SimpleAction {
        def apply(request: Request): Future[Response] = responseFuture
      })
      val routeParams = Map.empty[String, String]
      val globalSettings = None
      val error = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, headers, cookies, attributes, content, None, routeParams, globalSettings, error, false, version)

      val newRequest = request.withAction(action)
      newRequest.action must_== action
      newRequest must_!= request
    }
    "be changed error" in {
      val method = Methods.Get
      val uri = "/"
      val headers = Seq.empty[(HeaderName, Any)]
      val cookies = Seq.empty[Cookie]
      val attributes = Map.empty[String, Any]
      val content = ChannelBuffer.empty
      val responseFuture = Future.successful(Response())
      val actionOpt = Some(new SimpleAction {
        def apply(request: Request): Future[Response] = responseFuture
      })
      val routeParams = Map.empty[String, String]
      val globalSettings = None
      val error = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, headers, cookies, attributes, content, actionOpt, routeParams, globalSettings, error, false, version)
      println(request)
      val exception = new Exception()
      val newRequest = request.withError(exception)
      newRequest.error.get must_== exception
      newRequest must_!= request
    }
    "be changed routeParams" in {
      val method = Methods.Get
      val uri = "/"
      val headers = Seq.empty[(HeaderName, Any)]
      val cookies = Seq.empty[Cookie]
      val attributes = Map.empty[String, Any]
      val content = ChannelBuffer.empty
      val responseFuture = Future.successful(Response())
      val action = Some(new SimpleAction {
        def apply(request: Request): Future[Response] = responseFuture
      })
      val routeParams = Map.empty[String, String]
      val globalSettings = None
      val error = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, headers, cookies, attributes, content, action, routeParams, globalSettings, error, false, version)
      val newRouteParams = Map("userId" -> "userA")
      val newRequest = request.withRouteParams(newRouteParams)
      println(newRequest)
      newRequest.routeParams must_== newRouteParams
      newRequest must_!= request
    }
  }


}
