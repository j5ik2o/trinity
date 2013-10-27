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
      val actionOpt = Some(new SimpleAction {
        def apply(request: Request): Future[Response] = Future.successful(Response())
      })
      val routeParams = Map.empty[String, String]
      val globalSettingsOpt = None
      val errorOpt = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, actionOpt, routeParams, globalSettingsOpt, errorOpt, version)
      request.method must_== method
      request.uri must_== uri
      request.actionOpt must_== actionOpt
      request.routeParams must_== routeParams
      request.globalSettingsOpt must_== globalSettingsOpt
      request.errorOpt must_== errorOpt
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
      val globalSettingsOpt = None
      val errorOpt = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, Some(action), routeParams, globalSettingsOpt, errorOpt, version)
      request.execute(action) must_== responseFuture
    }
    "be changed actionOpt" in {
      val method = Method.Get
      val uri = "/"
      val responseFuture = Future.successful(Response())
      val action = new SimpleAction {
        def apply(request: Request): Future[Response] = responseFuture
      }
      val routeParams = Map.empty[String, String]
      val globalSettingsOpt = None
      val errorOpt = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, None, routeParams, globalSettingsOpt, errorOpt, version)

      val actionOpt = Some(action)
      val newRequest = request.withActionOpt(actionOpt)
      newRequest.actionOpt must_== actionOpt
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
      val globalSettingsOpt = None
      val errorOpt = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, actionOpt, routeParams, globalSettingsOpt, errorOpt, version)
      println(request)
      val exception = new Exception()
      val newRequest = request.withError(exception)
      newRequest.errorOpt.get must_== exception
      newRequest must_!= request
    }
    "be changed routeParams" in {
      val method = Method.Get
      val uri = "/"
      val responseFuture = Future.successful(Response())
      val actionOpt = Some(new SimpleAction {
        def apply(request: Request): Future[Response] = responseFuture
      })
      val routeParams = Map.empty[String, String]
      val globalSettingsOpt = None
      val errorOpt = None
      val version = ProtocolVersion.Http11

      val request = new RequestImpl(method, uri, actionOpt, routeParams, globalSettingsOpt, errorOpt, version)
      val newRouteParams = Map("userId" -> "userA")
      val newRequest = request.withRouteParams(newRouteParams)
      println(newRequest)
      newRequest.routeParams must_== newRouteParams
      newRequest must_!= request
    }
  }


}
