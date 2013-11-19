package org.sisioh.trinity.domain.mvc.http

import org.junit.runner.RunWith
import org.sisioh.trinity.domain.io.http.{ProtocolVersion, Method}
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class RequestImplSpec extends Specification {

  "request" should {
    "has action as some" in {
      val method = Method.Get
      val uri = "/"
      val action = Some(new SimpleAction {
        def apply(request: Request): Future[Response] = Future.successful(Response())
      })
      val routeParams = Map.empty[String, String]
      val globalSettings = None
      val error = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, action, routeParams, globalSettings, error, version)
      request.method must_== method
      request.uri must_== uri
      request.action must_== action
      request.routeParams must_== routeParams
      request.globalSettings must_== globalSettings
      request.error must_== error
      request.protocolVersion must_== version
    }
    "execute action" in {
      val method = Method.Get
      val uri = "/"
      val responseFuture = Future.successful(Response())
      val action = new SimpleAction {
        def apply(request: Request): Future[Response] = responseFuture
      }
      val routeParams = Map.empty[String, String]
      val globalSettings = None
      val error = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, Some(action), routeParams, globalSettings, error, version)
      request.execute(action) must_== responseFuture
    }
    "be changed actionOpt" in {
      val method = Method.Get
      val uri = "/"
      val responseFuture = Future.successful(Response())
      val action = Some(new SimpleAction {
        def apply(request: Request): Future[Response] = responseFuture
      })
      val routeParams = Map.empty[String, String]
      val globalSettings = None
      val error = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, None, routeParams, globalSettings, error, version)

      val newRequest = request.withActionOpt(action)
      newRequest.action must_== action
      newRequest must_!= request
    }
    "be changed errorOpt" in {
      val method = Method.Get
      val uri = "/"
      val responseFuture = Future.successful(Response())
      val actionOpt = Some(new SimpleAction {
        def apply(request: Request): Future[Response] = responseFuture
      })
      val routeParams = Map.empty[String, String]
      val globalSettings = None
      val error = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, actionOpt, routeParams, globalSettings, error, version)
      println(request)
      val exception = new Exception()
      val newRequest = request.withError(exception)
      newRequest.error.get must_== exception
      newRequest must_!= request
    }
    "be changed routeParams" in {
      val method = Method.Get
      val uri = "/"
      val responseFuture = Future.successful(Response())
      val action = Some(new SimpleAction {
        def apply(request: Request): Future[Response] = responseFuture
      })
      val routeParams = Map.empty[String, String]
      val globalSettings = None
      val error = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, action, routeParams, globalSettings, error, version)
      val newRouteParams = Map("userId" -> "userA")
      val newRequest = request.withRouteParams(newRouteParams)
      println(newRequest)
      newRequest.routeParams must_== newRouteParams
      newRequest must_!= request
    }
  }


}
