package io.kresult.problem

sealed class Problem(
  override val type: String,
  override val status: Int
) : ProblemDefinition {

  /**
   * Creates a JSON string representation of the problem
   *
   * This uses a rather naive, but dependency-free string builder approach to create standard
   * `application/json+problem` representation. However, virtually any JSON library could be used on implementation
   * side as well. As [RFC7807](https://datatracker.ietf.org/doc/html/rfc7807) specifies the JSOn schema pretty well,
   * no JSON library mappings like kotlinx-serialization or Jackson are provided.
   *
   * @param pretty If true, output JSOn will be pretty-printed
   */
  fun toJson(pretty: Boolean = false): String {
    val indent = if (pretty) " " else ""
    val newline = if (pretty) "\n" else ""

    val sb = StringBuilder()
    sb.append("{$newline")

    // Required fields (type and status)
    sb.append(indent).append(""""type": "${escapeJson(type)}",$newline""")
    sb.append(indent).append(""""status": $status""")

    // Optional fields
    title?.let {
      sb.append(",$newline")
      sb.append(indent).append(""""title": "${escapeJson(it)}"""")
    }
    detail?.let {
      sb.append(",$newline")
      sb.append(indent).append(""""detail": "${escapeJson(it)}"""")
    }
    instance?.let {
      sb.append(",$newline")
      sb.append(indent).append(""""instance": "${escapeJson(it)}"""")
    }
    sb.append("$newline}")

    return sb.toString()
  }

  private fun escapeJson(str: String): String {
    return str.replace("\"", "\\\"")
      .replace("\\", "\\\\")
      .replace("\n", "\\n")
      .replace("\r", "\\r")
      .replace("\t", "\\t")
  }

  /**
   * 40x HTTP Status Codes
   *
   * Indicates a client error
   */

  data class BadRequest(
    override val title: String = "The request was invalid",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("bad-request"), status = 400)

  data class Unauthorized(
    override val title: String = "Authentication credentials are required",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("unauthorized"), status = 401)

  data class PaymentRequired(
    override val title: String = "Payment is required to process this request",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("payment-required"), status = 402)

  data class Forbidden(
    override val title: String = "Access to this resource is forbidden",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("forbidden"), status = 403)

  data class NotFound(
    override val title: String = "The requested resource was not found",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("not-found"), status = 404)

  data class MethodNotAllowed(
    override val title: String = "The HTTP method is not allowed for this resource",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("method-not-allowed"), status = 405)

  data class NotAcceptable(
    override val title: String = "The requested format is not supported",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("not-acceptable"), status = 406)

  data class ProxyAuthenticationRequired(
    override val title: String = "Proxy authentication is required",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("proxy-authentication-required"), status = 407)

  data class RequestTimeout(
    override val title: String = "The request timed out",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("request-timeout"), status = 408)

  data class Conflict(
    override val title: String = "The request conflicts with the current state",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("conflict"), status = 409)

  data class Gone(
    override val title: String = "The resource is no longer available",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("gone"), status = 410)

  data class LengthRequired(
    override val title: String = "Content length header is required",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("length-required"), status = 411)

  data class PreconditionFailed(
    override val title: String = "Request preconditions failed",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("precondition-failed"), status = 412)

  data class PayloadTooLarge(
    override val title: String = "The request payload exceeds the size limit",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("payload-too-large"), status = 413)

  data class UriTooLong(
    override val title: String = "The request URI exceeds the length limit",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("uri-too-long"), status = 414)

  data class UnsupportedMediaType(
    override val title: String = "The request media type is not supported",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("unsupported-media-type"), status = 415)

  data class RangeNotSatisfiable(
    override val title: String = "The requested range cannot be satisfied",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("range-not-satisfiable"), status = 416)

  data class ExpectationFailed(
    override val title: String = "Server cannot meet the request expectations",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("expectation-failed"), status = 417)

  data class ImATeapot(
    override val title: String = "Server refuses to brew coffee with a teapot",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("im-a-teapot"), status = 418)

  data class MisdirectedRequest(
    override val title: String = "The request was directed to an invalid server",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("misdirected-request"), status = 421)

  data class UnprocessableEntity(
    override val title: String = "The request content is semantically invalid",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("unprocessable-entity"), status = 422)

  data class Locked(
    override val title: String = "The resource is locked",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("locked"), status = 423)

  data class FailedDependency(
    override val title: String = "The request failed due to a dependent request",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("failed-dependency"), status = 424)

  data class TooEarly(
    override val title: String = "The server is unwilling to process the request",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("too-early"), status = 425)

  data class UpgradeRequired(
    override val title: String = "The client must upgrade to continue",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("upgrade-required"), status = 426)

  data class PreconditionRequired(
    override val title: String = "The request requires precondition headers",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("precondition-required"), status = 428)

  data class TooManyRequests(
    override val title: String = "Request rate limit has been exceeded",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("too-many-requests"), status = 429)

  data class RequestHeaderFieldsTooLarge(
    override val title: String = "Request header fields exceed size limits",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("request-header-fields-too-large"), status = 431)

  data class UnavailableForLegalReasons(
    override val title: String = "Resource unavailable for legal reasons",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("unavailable-for-legal-reasons"), status = 451)

  /**
   * 50x HTTP Status Codes
   * Indicates a server error
   */

  data class InternalServerError(
    override val title: String = "An unexpected error occurred while processing the request",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("internal-server-error"), status = 500)

  data class NotImplemented(
    override val title: String = "The requested functionality is not implemented",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("not-implemented"), status = 501)

  data class BadGateway(
    override val title: String = "Invalid response received from upstream server",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("bad-gateway"), status = 502)

  data class ServiceUnavailable(
    override val title: String = "The service is temporarily unavailable",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("service-unavailable"), status = 503)

  data class GatewayTimeout(
    override val title: String = "Upstream server response timed out",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("gateway-timeout"), status = 504)

  data class HttpVersionNotSupported(
    override val title: String = "HTTP version used in the request is not supported",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("http-version-not-supported"), status = 505)

  data class VariantAlsoNegotiates(
    override val title: String = "Server detected an internal configuration error",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("variant-also-negotiates"), status = 506)

  data class InsufficientStorage(
    override val title: String = "Server has insufficient storage to complete the request",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("insufficient-storage"), status = 507)

  data class LoopDetected(
    override val title: String = "Server detected an infinite loop while processing the request",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("loop-detected"), status = 508)

  data class NotExtended(
    override val title: String = "Further extensions to the request are required",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("not-extended"), status = 510)

  data class NetworkAuthenticationRequired(
    override val title: String = "Network authentication is required to access the resource",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("network-authentication-required"), status = 511)

  /**
   * Unofficial 70x HTTP Status Codes
   *
   * Source: https://github.com/joho/7XX-rfc
   */

  data class Meh(
    override val title: String = "Meh",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("meh"), status = 701)

  data class Explosion(
    override val title: String = "Explosion",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("explosion"), status = 703)

  data class DeleteYourAccount(
    override val title: String = "Delete Your Account",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("delete-your-account"), status = 706)

  data class Unpossible(
    override val title: String = "Unpossible",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("unpossible"), status = 720)

  data class KnownUnknowns(
    override val title: String = "Known Unknowns",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("known-unknowns"), status = 721)

  data class UnknownUnknowns(
    override val title: String = "Unknown Unknowns",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("unknown-unknowns"), status = 722)

  data class Tricky(
    override val title: String = "Tricky",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("tricky"), status = 723)

  data class ThisLineShouldBeUnreachable(
    override val title: String = "This line should be unreachable",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("this-line-should-be-unreachable"), status = 724)

  data class ItWorksOnMyMachine(
    override val title: String = "It works on my machine",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("it-works-on-my-machine"), status = 725)

  data class ItsAFeatureNotABug(
    override val title: String = "It's a feature, not a bug",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("its-a-feature-not-a-bug"), status = 726)

  data class ThirtyTwoBitsIsPlenty(
    override val title: String = "32 bits is plenty",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("32-bits-is-plenty"), status = 727)

  data class ItWorksInMyTimezone(
    override val title: String = "It works in my timezone",
    override val detail: String? = null,
    override val instance: String? = null
  ) : Problem(type = typeUri("it-works-in-my-timezone"), status = 728)

  companion object {
    private const val BASE_URL = "https://kresult.io/problem"

    fun typeUri(slug: String) = """$BASE_URL/$slug"""
  }
}
