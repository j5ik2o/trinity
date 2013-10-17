package org.sisioh.trinity.domain.io.http

import scala.language.implicitConversions
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import org.jboss.netty.handler.codec.http.HttpResponseStatus.{ACCEPTED, BAD_GATEWAY, BAD_REQUEST, CONFLICT, CONTINUE, CREATED, EXPECTATION_FAILED, FAILED_DEPENDENCY, FORBIDDEN, FOUND, GATEWAY_TIMEOUT, GONE, HTTP_VERSION_NOT_SUPPORTED, INSUFFICIENT_STORAGE, INTERNAL_SERVER_ERROR, LENGTH_REQUIRED, LOCKED, METHOD_NOT_ALLOWED, MOVED_PERMANENTLY, MULTIPLE_CHOICES, MULTI_STATUS, NON_AUTHORITATIVE_INFORMATION, NOT_ACCEPTABLE, NOT_EXTENDED, NOT_FOUND, NOT_IMPLEMENTED, NOT_MODIFIED, NO_CONTENT, OK, PARTIAL_CONTENT, PAYMENT_REQUIRED, PRECONDITION_FAILED, PROCESSING, PROXY_AUTHENTICATION_REQUIRED, REQUESTED_RANGE_NOT_SATISFIABLE, REQUEST_ENTITY_TOO_LARGE, REQUEST_TIMEOUT, REQUEST_URI_TOO_LONG, RESET_CONTENT, SEE_OTHER, SERVICE_UNAVAILABLE, SWITCHING_PROTOCOLS, TEMPORARY_REDIRECT, UNAUTHORIZED, UNORDERED_COLLECTION, UNPROCESSABLE_ENTITY, UNSUPPORTED_MEDIA_TYPE, UPGRADE_REQUIRED, USE_PROXY, VARIANT_ALSO_NEGOTIATES}

object ResponseStatus extends Enumeration {

  val Continue = Value(CONTINUE.getCode)
  val SwitchingProtocols = Value(SWITCHING_PROTOCOLS.getCode)
  val Processing = Value(PROCESSING.getCode)
  val Ok = Value(OK.getCode)
  val Created = Value(CREATED.getCode)
  val Accepted = Value(ACCEPTED.getCode)
  val NonAuthoritativeInformation = Value(NON_AUTHORITATIVE_INFORMATION.getCode)
  val NoContent = Value(NO_CONTENT.getCode)
  val ResetContent = Value(RESET_CONTENT.getCode)
  val PartialContent = Value(PARTIAL_CONTENT.getCode)
  val MultiStatus = Value(MULTI_STATUS.getCode)
  val MultiChoices = Value(MULTIPLE_CHOICES.getCode)
  val MovedPermanently = Value(MOVED_PERMANENTLY.getCode)
  val Found = Value(FOUND.getCode)
  val SeeOther = Value(SEE_OTHER.getCode)
  val NotModified = Value(NOT_MODIFIED.getCode)
  val UseProxy = Value(USE_PROXY.getCode)
  val TemporaryRedirect = Value(TEMPORARY_REDIRECT.getCode)
  val BadRequest = Value(BAD_REQUEST.getCode)
  val Unauthorized = Value(UNAUTHORIZED.getCode)
  val PaymentRequired = Value(PAYMENT_REQUIRED.getCode)
  val Forbidden = Value(FORBIDDEN.getCode)
  val NotFound = Value(NOT_FOUND.getCode)
  val MethodNotAllowed = Value(METHOD_NOT_ALLOWED.getCode)
  val NotAcceptable = Value(NOT_ACCEPTABLE.getCode)
  val ProxyAuthenticationRequired = Value(PROXY_AUTHENTICATION_REQUIRED.getCode)
  val RequestTimeout = Value(REQUEST_TIMEOUT.getCode)
  val Conflict = Value(CONFLICT.getCode)
  val Gone = Value(GONE.getCode)
  val LengthRequired = Value(LENGTH_REQUIRED.getCode)
  val PreconditionFailed = Value(PRECONDITION_FAILED.getCode)
  val RequestEntityTooLarge = Value(REQUEST_ENTITY_TOO_LARGE.getCode)
  val RequestURITooLong = Value(REQUEST_URI_TOO_LONG.getCode)
  val UnsupportedMediaType = Value(UNSUPPORTED_MEDIA_TYPE.getCode)
  val RequestedRangeNotSatisfiable = Value(REQUESTED_RANGE_NOT_SATISFIABLE.getCode)
  val ExpectationFailed = Value(EXPECTATION_FAILED.getCode)
  val UnprocessableEntity = Value(UNPROCESSABLE_ENTITY.getCode)
  val Locked = Value(LOCKED.getCode)
  val FailedDependency = Value(FAILED_DEPENDENCY.getCode)
  val UnorderedCollection = Value(UNORDERED_COLLECTION.getCode)
  val UpgradeRequired = Value(UPGRADE_REQUIRED.getCode)
  val InternalServerError = Value(INTERNAL_SERVER_ERROR.getCode)
  val NotImplemented = Value(NOT_IMPLEMENTED.getCode)
  val BadGateway = Value(BAD_GATEWAY.getCode)
  val ServiceUnavailable = Value(SERVICE_UNAVAILABLE.getCode)
  val GatewayTimeout = Value(GATEWAY_TIMEOUT.getCode)
  val HTTPVersionNotSupported = Value(HTTP_VERSION_NOT_SUPPORTED.getCode)
  val VariantAlsoNegotiates = Value(VARIANT_ALSO_NEGOTIATES.getCode)
  val InsufficientStorage = Value(INSUFFICIENT_STORAGE.getCode)
  val NotExtended = Value(NOT_EXTENDED.getCode)

  private[domain] implicit def toNetty(value: ResponseStatus.Value) =
    HttpResponseStatus.valueOf(value.id)

  private[domain] implicit def toTrinity(value: HttpResponseStatus) =
    ResponseStatus(value.getCode)

}
