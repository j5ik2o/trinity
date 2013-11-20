package org.sisioh.trinity.domain.mvc.controller

import org.sisioh.trinity.domain.io.http.Method
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{ResponseSupport, Request, Response}
import org.sisioh.trinity.domain.mvc.routing.RouteDef
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{PathPattern, PathPatternParser}
import scala.collection.mutable
import scala.concurrent.Future
import scala.util.matching.Regex

trait ScalatraLikeController extends ResponseSupport {

  protected val routeDefs = mutable.ListBuffer.empty[RouteDef]

  protected def get(path: String)(callback: Request => Future[Response])(implicit pathPatternParser: PathPatternParser) {
    addRoute(Method.Get, path)(callback)
  }

  protected def get(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                   (callback: Request => Future[Response]) {
    addRoute(Method.Get, regex, captureGroupNames)(callback)
  }

  protected def delete(path: String)(callback: Request => Future[Response])(implicit pathPatternParser: PathPatternParser) {
    addRoute(Method.Delete, path)(callback)
  }

  protected def delete(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                      (callback: Request => Future[Response]) {
    addRoute(Method.Delete, regex, captureGroupNames)(callback)
  }

  protected def post(path: String)(callback: Request => Future[Response])(implicit pathPatternParser: PathPatternParser) {
    addRoute(Method.Post, path)(callback)
  }

  protected def post(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                    (callback: Request => Future[Response]) {
    addRoute(Method.Post, regex, captureGroupNames)(callback)
  }

  protected def put(path: String)(callback: Request => Future[Response])(implicit pathPatternParser: PathPatternParser) {
    addRoute(Method.Put, path)(callback)
  }

  protected def put(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)(callback: Request => Future[Response]) {
    addRoute(Method.Put, regex, captureGroupNames)(callback)
  }

  protected def head(path: String)(callback: Request => Future[Response])(implicit pathPatternParser: PathPatternParser) {
    addRoute(Method.Head, path)(callback)
  }

  protected def head(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                    (callback: Request => Future[Response]) {
    addRoute(Method.Head, regex, captureGroupNames)(callback)
  }

  protected def patch(path: String)(callback: Request => Future[Response])(implicit pathPatternParser: PathPatternParser) {
    addRoute(Method.Post, path)(callback)
  }

  protected def patch(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)(callback: Request => Future[Response]) {
    addRoute(Method.Post, regex, captureGroupNames)(callback)
  }

  protected def addRoute(method: Method.Value, path: String)(callback: Request => Future[Response])
                        (implicit pathPatternParser: PathPatternParser) {
    val route = RouteDef(method, pathPatternParser(path), Action(callback))
    routeDefs.append(route)
  }

  protected def addRoute(method: Method.Value, regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                        (callback: Request => Future[Response]) {
    val route = RouteDef(method, PathPattern(regex, captureGroupNames), Action(callback))
    routeDefs.append(route)
  }

  def toRouteDefs: Seq[RouteDef] = routeDefs.toSeq


}
