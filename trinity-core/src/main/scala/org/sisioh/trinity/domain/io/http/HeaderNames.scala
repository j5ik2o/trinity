package org.sisioh.trinity.domain.io.http

import org.jboss.netty.handler.codec.http.HttpHeaders._
import org.sisioh.scala.toolbox.Enum
import org.sisioh.scala.toolbox.EnumEntry

sealed trait HeaderName extends EnumEntry {
  val asString: String
}

object HeaderNames extends Enum[HeaderName] {

  private case class AnyHeaderName(asString: String) extends HeaderName

  def ofAny(name: String): HeaderName = AnyHeaderName(name)

  case object Accept extends HeaderName {
    val asString = Names.ACCEPT
  }

  case object AcceptCharset extends HeaderName {
    val asString = Names.ACCEPT_CHARSET
  }

  case object AcceptEncoding extends HeaderName {
    val asString = Names.ACCEPT_ENCODING
  }

  case object AcceptLanguage extends HeaderName {
    val asString = Names.ACCEPT_LANGUAGE
  }

  case object AcceptRanges extends HeaderName {
    val asString = Names.ACCEPT_RANGES
  }

  case object AcceptPatch extends HeaderName {
    val asString: String = Names.ACCEPT_PATCH
  }

  case object AccessControlAllowCredentials extends HeaderName {
    val asString: String = Names.ACCESS_CONTROL_ALLOW_CREDENTIALS
  }


  case object AccessControlAllowHeaders extends HeaderName {
    val asString: String = Names.ACCESS_CONTROL_ALLOW_HEADERS
  }

  case object AccessControlAllowMethods extends HeaderName {
    val asString = Names.ACCESS_CONTROL_ALLOW_METHODS
  }

  case object AccessControlAllowOrigin extends HeaderName {
    val asString = Names.ACCESS_CONTROL_ALLOW_ORIGIN
  }

  case object AccessControlExposeHeaders extends HeaderName {
    val asString: String = Names.ACCESS_CONTROL_EXPOSE_HEADERS
  }

  case object AccessControlMaxAge extends HeaderName {
    val asString: String = Names.ACCESS_CONTROL_MAX_AGE
  }

  case object AccessControlRequestHeaders extends HeaderName {
    val asString: String = Names.ACCESS_CONTROL_REQUEST_HEADERS
  }

  case object AccessControlRequestMethod extends HeaderName {
    val asString: String = Names.ACCESS_CONTROL_REQUEST_METHOD
  }

  case object Age extends HeaderName {
    val asString: String = Names.AGE
  }

  case object Allow extends HeaderName {
    val asString: String = Names.ALLOW
  }

  case object Authorization extends HeaderName {
    val asString: String = Names.AUTHORIZATION
  }

  case object CacheControl extends HeaderName {
    val asString: String = Names.CACHE_CONTROL
  }

  case object Connection extends HeaderName {
    val asString: String = Names.CONNECTION
  }

  //  val CacheControl = Value(Names.CACHE_CONTROL)
  //  val Connection = Value(Names.CONNECTION)
  //
  //  val ContentBase = Value(Names.CONTENT_BASE)
  //  val ContentEncoding = Value(Names.CONTENT_ENCODING)
  //  val ContentLanguage = Value(Names.CONTENT_LANGUAGE)
  //  val ContentLength = Value(Names.CONTENT_LENGTH)
  //  val ContentLocation = Value(Names.CONTENT_LOCATION)
  //  val ContentTransferEncoding = Value(Names.CONTENT_TRANSFER_ENCODING)
  //  val ContentMD5 = Value(Names.CONTENT_MD5)
  //  val ContentRange = Value(Names.CONTENT_RANGE)
  //  val ContentType = Value(Names.CONTENT_TYPE)
  //
  //  val Cookie = Value(Names.COOKIE)
  //  val Date = Value(Names.DATE)
  //  val ETag = Value(Names.ETAG)
  //  val Expect = Value(Names.EXPECT)
  //  val Expires = Value(Names.EXPIRES)
  //  val From = Value(Names.FROM)
  //  val Host = Value(Names.HOST)
  //
  //  val IfMatch = Value(Names.IF_MATCH)
  //  val IfModifiedSince = Value(Names.IF_MODIFIED_SINCE)
  //  val IfNoneMatch = Value(Names.IF_NONE_MATCH)
  //  val IfRange = Value(Names.IF_RANGE)
  //  val IfUnmodifiedSince = Value(Names.IF_UNMODIFIED_SINCE)
  //
  //  val LastModified = Value(Names.LAST_MODIFIED)
  //  val Location = Value(Names.LOCATION)
  //  val MaxForwards = Value(Names.MAX_FORWARDS)
  //  val Origin = Value(Names.ORIGIN)
  //  val Pragma = Value(Names.PRAGMA)
  //
  //  val ProxyAuthenticate = Value(Names.PROXY_AUTHENTICATE)
  //  val ProxyAuthorization = Value(Names.PROXY_AUTHORIZATION)
  //
  //  val Range = Value(Names.RANGE)
  //  val Referer = Value(Names.REFERER)
  //  val RetryAfter = Value(Names.RETRY_AFTER)
  //
  //  val SecWebSocketKey1 = Value(Names.SEC_WEBSOCKET_KEY1)
  //  val SecWebSocketKey2 = Value(Names.SEC_WEBSOCKET_KEY2)
  //  val SecWebSocketLocation = Value(Names.SEC_WEBSOCKET_LOCATION)
  //  val SecWebSocketOrigin = Value(Names.SEC_WEBSOCKET_ORIGIN)
  //  val SecWebSocketProtocol = Value(Names.SEC_WEBSOCKET_PROTOCOL)
  //  val SecWebSocketVersion = Value(Names.SEC_WEBSOCKET_VERSION)
  //  val SecWebSocketKey = Value(Names.SEC_WEBSOCKET_KEY)
  //  val SecWebSocketAccept = Value(Names.SEC_WEBSOCKET_ACCEPT)
  //
  //  val Server = Value(Names.SERVER)
  //
  //  val SetCookie = Value(Names.SET_COOKIE)
  //  val SetCookie2 = Value(Names.SET_COOKIE2)
  //
  //  val TE = Value(Names.TE)
  //
  //  val Trailer = Value(Names.TRAILER)
  //
  //  val TransferEncoding = Value(Names.TRANSFER_ENCODING)
  //
  //  val Upgrade = Value(Names.UPGRADE)
  //  val UserAgent = Value(Names.USER_AGENT)
  //
  //  val Vary = Value(Names.VARY)
  //  val Via = Value(Names.VIA)
  //  val Warning = Value(Names.WARNING)
  //
  //  val WebSocketLocation = Value(Names.WEBSOCKET_LOCATION)
  //  val WebSocketOrigin = Value(Names.WEBSOCKET_ORIGIN)
  //  val WebSocketProtocol = Value(Names.WEBSOCKET_PROTOCOL)
  //
  //  val WWWAuthenticate = Value(Names.WWW_AUTHENTICATE)

}
