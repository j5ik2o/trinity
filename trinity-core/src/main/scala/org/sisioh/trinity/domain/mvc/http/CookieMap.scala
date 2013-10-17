package org.sisioh.trinity.domain.mvc.http

import com.twitter.finagle.http.{CookieMap => FinagleCookieMap, Cookie => FinagleCookie}
import org.jboss.netty.handler.codec.http.{CookieEncoder => NettyCookieEncoder, CookieDecoder => NettyCookieDecoder, HttpHeaders}
import org.sisioh.trinity.domain.io.transport.codec.http.Cookie
import scala.collection.JavaConverters._
import scala.collection.immutable.MapLike

case class CookieMap(message: Message)
  extends Map[String, Cookie]
  with MapLike[String, Cookie, CookieMap] {

  def isValid = _isValid

  private[this] var _isValid = true

  private[this] val cookieHeaderName =
    if (message.isRequest)
      HttpHeaders.Names.COOKIE
    else
      HttpHeaders.Names.SET_COOKIE

  var underlying = (for {
    cookieHeader <- message.getHeaders(cookieHeaderName)
    cookie <- decodeCookies(cookieHeader)
  } yield {
    (cookie.name, cookie)
  }).toMap

  private[this] def decodeCookies(header: String): Iterable[Cookie] = {
    val decoder = new NettyCookieDecoder
    try {
      decoder.decode(header).asScala map {
        Cookie(_)
      }
    } catch {
      case e: IllegalArgumentException =>
        _isValid = false
        Nil
    }
  }

  protected def rewriteCookieHeaders = {
    // Clear all cookies - there may be more than one with this name.
    val newMessage = message.withoutHeader(cookieHeaderName)
    // Add cookies back again
    val headers = map {
      case (_, cookie) =>
        val encoder = new NettyCookieEncoder(message.isResponse)
        encoder.addCookie(cookie.underlying)
        encoder.encode()
    }.toSeq
    newMessage.withHeader(cookieHeaderName, headers)
  }

  def get(key: String): Option[Cookie] = underlying.get(key)

  def iterator: Iterator[(String, Cookie)] = underlying.iterator

  def -(key: String): CookieMap = {
    underlying -= (key)
    new CookieMap(rewriteCookieHeaders)
  }

  def +[B1 >: Cookie](kv: (String, B1)): Map[String, B1] = {
    underlying += (kv)
    new CookieMap(rewriteCookieHeaders)
  }
}
