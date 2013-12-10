package org.sisioh.trinity.domain.io.http

import org.jboss.netty.handler.codec.http.HttpResponseStatus
import org.jboss.netty.handler.codec.http.HttpResponseStatus.{ACCEPTED, BAD_GATEWAY, BAD_REQUEST, CONFLICT, CONTINUE, CREATED, EXPECTATION_FAILED, FAILED_DEPENDENCY, FORBIDDEN, FOUND, GATEWAY_TIMEOUT, GONE, HTTP_VERSION_NOT_SUPPORTED, INSUFFICIENT_STORAGE, INTERNAL_SERVER_ERROR, LENGTH_REQUIRED, LOCKED, METHOD_NOT_ALLOWED, MOVED_PERMANENTLY, MULTIPLE_CHOICES, MULTI_STATUS, NON_AUTHORITATIVE_INFORMATION, NOT_ACCEPTABLE, NOT_EXTENDED, NOT_FOUND, NOT_IMPLEMENTED, NOT_MODIFIED, NO_CONTENT, OK, PARTIAL_CONTENT, PAYMENT_REQUIRED, PRECONDITION_FAILED, PROCESSING, PROXY_AUTHENTICATION_REQUIRED, REQUESTED_RANGE_NOT_SATISFIABLE, REQUEST_ENTITY_TOO_LARGE, REQUEST_TIMEOUT, REQUEST_URI_TOO_LONG, RESET_CONTENT, SEE_OTHER, SERVICE_UNAVAILABLE, SWITCHING_PROTOCOLS, TEMPORARY_REDIRECT, UNAUTHORIZED, UNORDERED_COLLECTION, UNPROCESSABLE_ENTITY, UNSUPPORTED_MEDIA_TYPE, UPGRADE_REQUIRED, USE_PROXY, VARIANT_ALSO_NEGOTIATES}
import scala.language.implicitConversions

object ResponseStatus extends Enumeration {

  private def withValue(value: HttpResponseStatus) =
    Value(value.getCode, value.getReasonPhrase)

  val Continue = withValue(CONTINUE)
  val SwitchingProtocols = withValue(SWITCHING_PROTOCOLS)
  val Processing = withValue(PROCESSING)
  val Ok = withValue(OK)
  val Created = withValue(CREATED)
  val Accepted = withValue(ACCEPTED)
  val NonAuthoritativeInformation = withValue(NON_AUTHORITATIVE_INFORMATION)
  val NoContent = withValue(NO_CONTENT)
  val ResetContent = withValue(RESET_CONTENT)
  val PartialContent = withValue(PARTIAL_CONTENT)
  val MultiStatus = withValue(MULTI_STATUS)
  val MultiChoices = withValue(MULTIPLE_CHOICES)
  val MovedPermanently = withValue(MOVED_PERMANENTLY)
  val Found = withValue(FOUND)
  val SeeOther = withValue(SEE_OTHER)
  val NotModified = withValue(NOT_MODIFIED)
  val UseProxy = withValue(USE_PROXY)
  val TemporaryRedirect = withValue(TEMPORARY_REDIRECT)
  val BadRequest = withValue(BAD_REQUEST)
  val Unauthorized = withValue(UNAUTHORIZED)
  val PaymentRequired = withValue(PAYMENT_REQUIRED)
  val Forbidden = withValue(FORBIDDEN)
  val NotFound = withValue(NOT_FOUND)
  val MethodNotAllowed = withValue(METHOD_NOT_ALLOWED)
  val NotAcceptable = withValue(NOT_ACCEPTABLE)
  val ProxyAuthenticationRequired = withValue(PROXY_AUTHENTICATION_REQUIRED)
  val RequestTimeout = withValue(REQUEST_TIMEOUT)
  val Conflict = withValue(CONFLICT)
  val Gone = withValue(GONE)
  val LengthRequired = withValue(LENGTH_REQUIRED)
  val PreconditionFailed = withValue(PRECONDITION_FAILED)
  val RequestEntityTooLarge = withValue(REQUEST_ENTITY_TOO_LARGE)
  val RequestURITooLong = withValue(REQUEST_URI_TOO_LONG)
  val UnsupportedMediaType = withValue(UNSUPPORTED_MEDIA_TYPE)
  val RequestedRangeNotSatisfiable = withValue(REQUESTED_RANGE_NOT_SATISFIABLE)
  val ExpectationFailed = withValue(EXPECTATION_FAILED)
  val UnprocessableEntity = withValue(UNPROCESSABLE_ENTITY)
  val Locked = withValue(LOCKED)
  val FailedDependency = withValue(FAILED_DEPENDENCY)
  val UnorderedCollection = withValue(UNORDERED_COLLECTION)
  val UpgradeRequired = withValue(UPGRADE_REQUIRED)
  val InternalServerError = withValue(INTERNAL_SERVER_ERROR)
  val NotImplemented = withValue(NOT_IMPLEMENTED)
  val BadGateway = withValue(BAD_GATEWAY)
  val ServiceUnavailable = withValue(SERVICE_UNAVAILABLE)
  val GatewayTimeout = withValue(GATEWAY_TIMEOUT)
  val HTTPVersionNotSupported = withValue(HTTP_VERSION_NOT_SUPPORTED)
  val VariantAlsoNegotiates = withValue(VARIANT_ALSO_NEGOTIATES)
  val InsufficientStorage = withValue(INSUFFICIENT_STORAGE)
  val NotExtended = withValue(NOT_EXTENDED)

  private[domain] implicit def toNetty(value: ResponseStatus.Value) =
    HttpResponseStatus.valueOf(value.id)

  private[domain] implicit def toTrinity(value: HttpResponseStatus) =
    ResponseStatus(value.getCode)

}
