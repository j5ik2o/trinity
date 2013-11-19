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

  def valueOf(name: String): HeaderName =
    values.find(_.asString == name).getOrElse(ofAny(name))

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

  case object ContentBase extends HeaderName {
    val asString: String = Names.CONTENT_BASE
  }

  case object ContentEncoding extends HeaderName {
    val asString: String = Names.CONTENT_ENCODING
  }

  case object ContentLanguage extends HeaderName {
    val asString: String = Names.CONTENT_LANGUAGE
  }

  case object ContentLength extends HeaderName {
    val asString: String = Names.CONTENT_LENGTH
  }

  case object ContentLocation extends HeaderName {
    val asString: String = Names.CONTENT_LOCATION
  }

  case object ContentTransferEncoding extends HeaderName {
    val asString: String = Names.CONTENT_TRANSFER_ENCODING
  }

  case object ContentMD5 extends HeaderName {
    val asString: String = Names.CONTENT_MD5
  }

  case object ContentRange extends HeaderName {
    val asString: String = Names.CONTENT_RANGE
  }

  case object ContentType extends HeaderName {
    val asString: String = Names.CONTENT_TYPE
  }

  case object Cookie extends HeaderName {
    val asString: String = Names.COOKIE
  }

  case object Date extends HeaderName {
    val asString: String = Names.DATE
  }

  case object ETag extends HeaderName {
    val asString: String = Names.ETAG
  }

  case object Expect extends HeaderName {
    val asString: String = Names.EXPECT
  }

  case object Expires extends HeaderName {
    val asString: String = Names.EXPIRES
  }

  case object From extends HeaderName {
    val asString: String = Names.FROM
  }

  case object Host extends HeaderName {
    val asString: String = Names.HOST
  }

  case object IfMatch extends HeaderName {
    val asString: String = Names.IF_MATCH
  }

  case object IfModifiedSince extends HeaderName {
    val asString: String = Names.IF_MODIFIED_SINCE
  }

  case object IfNoneMatch extends HeaderName {
    val asString: String = Names.IF_NONE_MATCH
  }

  case object IfRange extends HeaderName {
    val asString: String = Names.IF_RANGE
  }

  case object IfUnmodifiedSince extends HeaderName {
    val asString: String = Names.IF_UNMODIFIED_SINCE
  }

  case object LastModified extends HeaderName {
    val asString: String = Names.LAST_MODIFIED
  }

  case object Location extends HeaderName {
    val asString: String = Names.LOCATION
  }

  case object MaxForwards extends HeaderName {
    val asString: String = Names.MAX_FORWARDS
  }

  case object Origin extends HeaderName {
    val asString: String = Names.ORIGIN
  }

  case object Pragma extends HeaderName {
    val asString: String = Names.PRAGMA
  }

  case object ProxyAuthenticate extends HeaderName {
    val asString: String = Names.PROXY_AUTHENTICATE
  }

  case object ProxyAuthorization extends HeaderName {
    val asString: String = Names.PROXY_AUTHORIZATION
  }

  case object Referer extends HeaderName {
    val asString: String = Names.REFERER
  }

  case object RetryAfter extends HeaderName {
    val asString: String = Names.RETRY_AFTER
  }

  case object SecWebSocketKey1 extends HeaderName {
    val asString: String = Names.SEC_WEBSOCKET_KEY1
  }

  case object SecWebSocketKey2 extends HeaderName {
    val asString: String = Names.SEC_WEBSOCKET_KEY2
  }

  case object SecWebSocketLocation extends HeaderName {
    val asString: String = Names.SEC_WEBSOCKET_LOCATION
  }

  case object SecWebSocketOrigin extends HeaderName {
    val asString: String = Names.SEC_WEBSOCKET_ORIGIN
  }

  case object SecWebSocketProtocol extends HeaderName {
    val asString: String = Names.SEC_WEBSOCKET_PROTOCOL
  }

  case object SecWebSocketVersion extends HeaderName {
    val asString: String = Names.SEC_WEBSOCKET_VERSION
  }

  case object SecWebSocketKey extends HeaderName {
    val asString: String = Names.SEC_WEBSOCKET_KEY
  }

  case object SecWebSocketAccept extends HeaderName {
    val asString: String = Names.SEC_WEBSOCKET_ACCEPT
  }

  case object Server extends HeaderName {
    val asString: String = Names.SERVER
  }

  case object SetCookie extends HeaderName {
    val asString: String = Names.SET_COOKIE
  }

  case object SetCookie2 extends HeaderName {
    val asString: String = Names.SET_COOKIE2
  }

  case object Te extends HeaderName {
    val asString: String = Names.TE
  }

  case object Trailer extends HeaderName {
    val asString: String = Names.TRAILER
  }

  case object TransferEncoding extends HeaderName {
    val asString: String = Names.TRANSFER_ENCODING
  }

  case object Upgrade extends HeaderName {
    val asString: String = Names.UPGRADE
  }

  case object UserAgent extends HeaderName {
    val asString: String = Names.USER_AGENT
  }

  case object Vary extends HeaderName {
    val asString: String = Names.VARY
  }

  case object Via extends HeaderName {
    val asString: String = Names.VIA
  }

  case object Warning extends HeaderName {
    val asString: String = Names.WARNING
  }

  case object WebSocketLocation extends HeaderName {
    val asString: String = Names.WEBSOCKET_LOCATION
  }

  case object WebSocketOrigin extends HeaderName {
    val asString: String = Names.WEBSOCKET_ORIGIN
  }

  case object WebSocketProtocol extends HeaderName {
    val asString: String = Names.WEBSOCKET_PROTOCOL
  }

  case object WWWAuthenticate extends HeaderName {
    val asString: String = Names.WWW_AUTHENTICATE
  }

  Accept % AcceptCharset %
    AcceptEncoding % AcceptLanguage % AcceptRanges %
    AcceptPatch % AccessControlAllowCredentials % AccessControlAllowHeaders %
    // --- for Access
    AccessControlAllowMethods % AccessControlAllowOrigin % AccessControlExposeHeaders %
    AccessControlMaxAge % AccessControlRequestHeaders % AccessControlRequestMethod %
    // ---
    Age % Allow % Authorization % CacheControl % Connection %
    // --- for Content
    ContentBase % ContentEncoding %
    ContentLanguage % ContentLength % ContentLocation % ContentTransferEncoding %
    ContentMD5 % ContentRange % ContentType %
    Cookie %
    Date %
    ETag %
    Expect %
    Expires %
    From %
    Host %
    // ---
    IfMatch % IfModifiedSince % IfNoneMatch % IfRange % IfUnmodifiedSince %
    LastModified %
    Location %
    MaxForwards %
    Origin %
    Pragma %
    // --- Proxy
    ProxyAuthenticate % ProxyAuthorization %
    // ---
    Referer % RetryAfter %
    SecWebSocketKey1 % SecWebSocketKey2 %
    SecWebSocketLocation % SecWebSocketOrigin %
    SecWebSocketProtocol % SecWebSocketVersion %
    Server %
    SetCookie % SetCookie2 %
    Te %
    Trailer %
    TransferEncoding %
    Upgrade %
    UserAgent %
    Vary %
    Via %
    Warning %
    WebSocketLocation % WebSocketOrigin % WebSocketProtocol %
    WWWAuthenticate


}
