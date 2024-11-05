package io.kresult.integration.quarkus.openapi

import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse

// 4xx problems

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.BadRequest] under certain conditions
 */
@APIResponse(
  responseCode = "400",
  description = "Bad Request - Incomplete or invalid data was supplied with the request. Indicates that one or more " +
    "request parameters were missing, wrong or malformed.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class BadRequestApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.Unauthorized] under certain conditions
 */
@APIResponse(
  responseCode = "401",
  description = "Unauthorized - Authentication is required to access this resource. Valid credentials were not " +
    "provided or the provided credentials are invalid.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class UnauthorizedApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.PaymentRequired] under certain
 * conditions
 */
@APIResponse(
  responseCode = "402",
  description = "Payment Required - The requested operation requires payment to proceed. This may indicate that a " +
    "subscription or payment is needed.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class PaymentRequiredApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.Forbidden] under certain conditions
 */
@APIResponse(
  responseCode = "403",
  description = "Forbidden - The server understood the request but refuses to authorize it. Unlike 401, " +
    "authentication will not help.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ForbiddenApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.NotFound] under certain conditions
 */
@APIResponse(
  responseCode = "404",
  description = "Not Found - The requested resource could not be found on the server. The resource may have been " +
    "deleted or may have never existed.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class NotFoundApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.MethodNotAllowed] under certain
 * conditions
 */
@APIResponse(
  responseCode = "405",
  description = "Method Not Allowed - The HTTP method used is not supported for this resource. Check the Allow " +
    "header for supported methods.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class MethodNotAllowedApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.NotAcceptable] under certain
 * conditions
 */
@APIResponse(
  responseCode = "406",
  description = "Not Acceptable - The requested resource cannot be provided in the format specified in the Accept" +
    "header.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class NotAcceptableApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.ProxyAuthenticationRequired] under
 * certain conditions
 */
@APIResponse(
  responseCode = "407",
  description = "Proxy Authentication Required - Authentication with the proxy server is required before the request " +
    "can proceed.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ProxyAuthenticationRequiredApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.RequestTimeout] under certain
 * conditions
 */
@APIResponse(
  responseCode = "408",
  description = "Request Timeout - The server timed out waiting for the request to complete.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class RequestTimeoutApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.Conflict] under certain conditions
 */
@APIResponse(
  responseCode = "409",
  description = "Conflict - The request conflicts with the current state of the server. Often occurs with concurrent " +
    "modifications or version conflicts.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ConflictApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.Gone] under certain conditions
 */
@APIResponse(
  responseCode = "410",
  description = "Gone - The requested resource is no longer available and will not be available again. Unlike 404, " +
    "this is expected to be permanent.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class GoneApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.LengthRequired] under certain
 * conditions
 */
@APIResponse(
  responseCode = "411",
  description = "Length Required - The request lacks a required Content-Length header.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class LengthRequiredApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.PreconditionFailed] under certain
 * conditions
 */
@APIResponse(
  responseCode = "412",
  description = "Precondition Failed - One or more conditions in the request headers evaluated to false.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class PreconditionFailedApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.PayloadTooLarge] under certain
 * conditions
 */
@APIResponse(
  responseCode = "413",
  description = "Payload Too Large - The request payload exceeds the server's size limits. Check documentation for " +
    "size restrictions.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class PayloadTooLargeApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.UriTooLong] under certain conditions
 */
@APIResponse(
  responseCode = "414",
  description = "URI Too Long - The request URI exceeds the maximum length the server can process.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class UriTooLongApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.UnsupportedMediaType] under certain
 * conditions
 */
@APIResponse(
  responseCode = "415",
  description = "Unsupported Media Type - The Content-Type of the request is not supported by this endpoint.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class UnsupportedMediaTypeApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.RangeNotSatisfiable] under certain
 * conditions
 */
@APIResponse(
  responseCode = "416",
  description = "Range Not Satisfiable - The requested range of the resource cannot be provided.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class RangeNotSatisfiableApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.ExpectationFailed] under certain
 * conditions
 */
@APIResponse(
  responseCode = "417",
  description = "Expectation Failed - The server cannot meet the requirements specified in the Expect request header.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ExpectationFailedApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.UnprocessableEntity] under certain
 * conditions
 */
@APIResponse(
  responseCode = "422",
  description = "Unprocessable Entity - The request was well-formed but contains semantic errors that prevent " +
    "processing.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class UnprocessableEntityApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.Locked] under certain conditions
 */
@APIResponse(
  responseCode = "423",
  description = "Locked - The requested resource is locked and cannot be accessed.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class LockedApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.FailedDependency] under certain
 * conditions
 */
@APIResponse(
  responseCode = "424",
  description = "Failed Dependency - The request failed because it depends on another request that failed.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class FailedDependencyApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.TooEarly] under certain conditions
 */
@APIResponse(
  responseCode = "425",
  description = "Too Early - The server is unwilling to risk processing a request that might be replayed.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class TooEarlyApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.UpgradeRequired] under certain
 * conditions
 */
@APIResponse(
  responseCode = "426",
  description = "Upgrade Required - The client must upgrade their protocol version to continue.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class UpgradeRequiredApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.PreconditionRequired] under certain
 * conditions
 */
@APIResponse(
  responseCode = "428",
  description = "Precondition Required - The server requires conditional requests for this resource.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class PreconditionRequiredApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.TooManyRequests] under certain
 * conditions
 */
@APIResponse(
  responseCode = "429",
  description = "Too Many Requests - The client has exceeded their rate limit. Wait before retrying and check " +
    "rate limit headers.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class TooManyRequestsApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.RequestHeaderFieldsTooLarge] under
 * certain conditions
 */
@APIResponse(
  responseCode = "431",
  description = "Request Header Fields Too Large - The request headers exceed the server's size limits.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class RequestHeaderFieldsTooLargeApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.UnavailableForLegalReasons] under
 * certain conditions
 */
@APIResponse(
  responseCode = "451",
  description = "Unavailable For Legal Reasons - Access to the resource is forbidden for legal reasons.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class UnavailableForLegalReasonsApiResponse

// 5xx

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.InternalServerError] under certain
 * conditions
 */
@APIResponse(
  responseCode = "500",
  description = "Internal Server Error - An unexpected error occurred while processing the request. This indicates a " +
    "problem with the server that needs to be investigated.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class InternalServerErrorApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.NotImplemented] under certain
 * conditions
 */
@APIResponse(
  responseCode = "501",
  description = "Not Implemented - The functionality required to complete the request has not been implemented. " +
    "This is a temporary condition and may be available in the future.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class NotImplementedApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.BadGateway] under certain conditions
 */
@APIResponse(
  responseCode = "502",
  description = "Bad Gateway - The server received an invalid response from an upstream server while attempting to " +
    "fulfill the request.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class BadGatewayApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.ServiceUnavailable] under certain
 * conditions
 */
@APIResponse(
  responseCode = "503",
  description = "Service Unavailable - The server is temporarily unable to handle the request due to being " +
    "overloaded or down for maintenance. Try again later.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ServiceUnavailableApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.GatewayTimeout] under certain
 * conditions
 */
@APIResponse(
  responseCode = "504",
  description = "Gateway Timeout - The server did not receive a timely response from an upstream server while " +
    "attempting to fulfill the request.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class GatewayTimeoutApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.HttpVersionNotSupported] under
 * certain conditions
 */
@APIResponse(
  responseCode = "505",
  description = "HTTP Version Not Supported - The server does not support the HTTP protocol version used in the request.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class HttpVersionNotSupportedApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.VariantAlsoNegotiates] under
 * certain conditions
 */
@APIResponse(
  responseCode = "506",
  description = "Variant Also Negotiates - The server has detected an internal configuration error where the chosen " +
    "variant resource is configured to engage in content negotiation itself.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class VariantAlsoNegotiatesApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.InsufficientStorage] under certain
 * conditions
 */
@APIResponse(
  responseCode = "507",
  description = "Insufficient Storage - The server is unable to store the representation needed to complete the request " +
    "due to insufficient storage capacity.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class InsufficientStorageApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.LoopDetected] under certain conditions
 */
@APIResponse(
  responseCode = "508",
  description = "Loop Detected - The server terminated an operation because it encountered an infinite loop while " +
    "processing a request.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class LoopDetectedApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.NotExtended] under certain conditions
 */
@APIResponse(
  responseCode = "510",
  description = "Not Extended - Further extensions to the request are required for the server to fulfill it.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class NotExtendedApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.NetworkAuthenticationRequired] under
 * certain conditions
 */
@APIResponse(
  responseCode = "511",
  description = "Network Authentication Required - The client needs to authenticate to gain network access before " +
    "accessing the requested resource.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class NetworkAuthenticationRequiredApiResponse

// 7xx

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.Meh] under certain conditions
 */
@APIResponse(
  responseCode = "701",
  description = "Meh - The server does not care about the request. The operation was not important enough to process.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class MehApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.Explosion] under certain conditions
 */
@APIResponse(
  responseCode = "703",
  description = "Explosion - Something exploded in the server. This is a non-recoverable error indicating severe " +
    "internal problems.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ExplosionApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.DeleteYourAccount] under certain
 * conditions
 */
@APIResponse(
  responseCode = "706",
  description = "Delete Your Account - The server suggests that the client should delete their account due to " +
    "inappropriate or malicious behavior.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class DeleteYourAccountApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.Unpossible] under certain conditions
 */
@APIResponse(
  responseCode = "720",
  description = "Unpossible - The request cannot possibly be fulfilled due to fundamental logical contradictions.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class UnpossibleApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.KnownUnknowns] under certain
 * conditions
 */
@APIResponse(
  responseCode = "721",
  description = "Known Unknowns - The server knows that it doesn't know how to handle this request.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class KnownUnknownsApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.UnknownUnknowns] under certain
 * conditions
 */
@APIResponse(
  responseCode = "722",
  description = "Unknown Unknowns - The server doesn't know what it doesn't know about handling this request.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class UnknownUnknownsApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.Tricky] under certain conditions
 */
@APIResponse(
  responseCode = "723",
  description = "Tricky - The request appears deceptively simple but is actually very complicated or impossible.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class TrickyApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.ThisLineShouldBeUnreachable] under
 * certain conditions
 */
@APIResponse(
  responseCode = "724",
  description = "This Line Should Be Unreachable - The server reached a code path that should not be possible under " +
    "normal circumstances.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ThisLineShouldBeUnreachableApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.ItWorksOnMyMachine] under certain
 * conditions
 */
@APIResponse(
  responseCode = "725",
  description = "It Works On My Machine - The server couldn't reproduce the error in development but it's happening " +
    "in production.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ItWorksOnMyMachineApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.ItsAFeatureNotABug] under certain
 * conditions
 */
@APIResponse(
  responseCode = "726",
  description = "It's A Feature Not A Bug - The server is behaving unexpectedly but claims this is the intended behavior.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ItsAFeatureNotABugApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.ThirtyTwoBitsIsPlenty] under certain
 * conditions
 */
@APIResponse(
  responseCode = "727",
  description = "32 Bits Is Plenty - The server encountered an integer overflow or similar architectural limit.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ThirtyTwoBitsIsPlentyApiResponse

/**
 * Indicates that an OpenAPI operation will return an [io.kresult.problem.Problem.ItWorksInMyTimezone] under certain
 * conditions
 */
@APIResponse(
  responseCode = "728",
  description = "It Works In My Timezone - The server encountered timezone-related issues that only manifest in " +
    "certain regions.",
  content = [
    Content(
      mediaType = "application/problem+json",
      schema = Schema(implementation = ProblemResponseSchema::class)
    )
  ]
)
annotation class ItWorksInMyTimezoneApiResponse
