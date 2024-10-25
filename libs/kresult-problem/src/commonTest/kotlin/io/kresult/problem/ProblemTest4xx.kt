package io.kresult.problem

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ProblemTest4xx {
  @Test
  fun `BadRequest can be built and printed to JSON`() {
    val problem = Problem.BadRequest()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/bad-request","status": 400,"title": "The request was invalid"}"""
  }

  @Test
  fun `Unauthorized can be built and printed to JSON`() {
    val problem = Problem.Unauthorized()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/unauthorized","status": 401,"title": "Authentication credentials are required"}"""
  }

  @Test
  fun `PaymentRequired can be built and printed to JSON`() {
    val problem = Problem.PaymentRequired()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/payment-required","status": 402,"title": "Payment is required to process this request"}"""
  }

  @Test
  fun `Forbidden can be built and printed to JSON`() {
    val problem = Problem.Forbidden()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/forbidden","status": 403,"title": "Access to this resource is forbidden"}"""
  }

  @Test
  fun `NotFound can be built and printed to JSON`() {
    val problem = Problem.NotFound()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/not-found","status": 404,"title": "The requested resource was not found"}"""
  }

  @Test
  fun `MethodNotAllowed can be built and printed to JSON`() {
    val problem = Problem.MethodNotAllowed()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/method-not-allowed","status": 405,"title": "The HTTP method is not allowed for this resource"}"""
  }

  @Test
  fun `NotAcceptable can be built and printed to JSON`() {
    val problem = Problem.NotAcceptable()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/not-acceptable","status": 406,"title": "The requested format is not supported"}"""
  }

  @Test
  fun `ProxyAuthenticationRequired can be built and printed to JSON`() {
    val problem = Problem.ProxyAuthenticationRequired()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/proxy-authentication-required","status": 407,"title": "Proxy authentication is required"}"""
  }

  @Test
  fun `RequestTimeout can be built and printed to JSON`() {
    val problem = Problem.RequestTimeout()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/request-timeout","status": 408,"title": "The request timed out"}"""
  }

  @Test
  fun `Conflict can be built and printed to JSON`() {
    val problem = Problem.Conflict()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/conflict","status": 409,"title": "The request conflicts with the current state"}"""
  }

  @Test
  fun `Gone can be built and printed to JSON`() {
    val problem = Problem.Gone()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/gone","status": 410,"title": "The resource is no longer available"}"""
  }

  @Test
  fun `LengthRequired can be built and printed to JSON`() {
    val problem = Problem.LengthRequired()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/length-required","status": 411,"title": "Content length header is required"}"""
  }

  @Test
  fun `PreconditionFailed can be built and printed to JSON`() {
    val problem = Problem.PreconditionFailed()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/precondition-failed","status": 412,"title": "Request preconditions failed"}"""
  }

  @Test
  fun `PayloadTooLarge can be built and printed to JSON`() {
    val problem = Problem.PayloadTooLarge()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/payload-too-large","status": 413,"title": "The request payload exceeds the size limit"}"""
  }

  @Test
  fun `UriTooLong can be built and printed to JSON`() {
    val problem = Problem.UriTooLong()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/uri-too-long","status": 414,"title": "The request URI exceeds the length limit"}"""
  }

  @Test
  fun `UnsupportedMediaType can be built and printed to JSON`() {
    val problem = Problem.UnsupportedMediaType()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/unsupported-media-type","status": 415,"title": "The request media type is not supported"}"""
  }

  @Test
  fun `RangeNotSatisfiable can be built and printed to JSON`() {
    val problem = Problem.RangeNotSatisfiable()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/range-not-satisfiable","status": 416,"title": "The requested range cannot be satisfied"}"""
  }

  @Test
  fun `ExpectationFailed can be built and printed to JSON`() {
    val problem = Problem.ExpectationFailed()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/expectation-failed","status": 417,"title": "Server cannot meet the request expectations"}"""
  }

  @Test
  fun `ImATeapot can be built and printed to JSON`() {
    val problem = Problem.ImATeapot()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/im-a-teapot","status": 418,"title": "Server refuses to brew coffee with a teapot"}"""
  }

  @Test
  fun `MisdirectedRequest can be built and printed to JSON`() {
    val problem = Problem.MisdirectedRequest()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/misdirected-request","status": 421,"title": "The request was directed to an invalid server"}"""
  }

  @Test
  fun `UnprocessableEntity can be built and printed to JSON`() {
    val problem = Problem.UnprocessableEntity()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/unprocessable-entity","status": 422,"title": "The request content is semantically invalid"}"""
  }

  @Test
  fun `Locked can be built and printed to JSON`() {
    val problem = Problem.Locked()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/locked","status": 423,"title": "The resource is locked"}"""
  }

  @Test
  fun `FailedDependency can be built and printed to JSON`() {
    val problem = Problem.FailedDependency()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/failed-dependency","status": 424,"title": "The request failed due to a dependent request"}"""
  }

  @Test
  fun `TooEarly can be built and printed to JSON`() {
    val problem = Problem.TooEarly()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/too-early","status": 425,"title": "The server is unwilling to process the request"}"""
  }

  @Test
  fun `UpgradeRequired can be built and printed to JSON`() {
    val problem = Problem.UpgradeRequired()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/upgrade-required","status": 426,"title": "The client must upgrade to continue"}"""
  }

  @Test
  fun `PreconditionRequired can be built and printed to JSON`() {
    val problem = Problem.PreconditionRequired()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/precondition-required","status": 428,"title": "The request requires precondition headers"}"""
  }

  @Test
  fun `TooManyRequests can be built and printed to JSON`() {
    val problem = Problem.TooManyRequests()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/too-many-requests","status": 429,"title": "Request rate limit has been exceeded"}"""
  }

  @Test
  fun `RequestHeaderFieldsTooLarge can be built and printed to JSON`() {
    val problem = Problem.RequestHeaderFieldsTooLarge()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/request-header-fields-too-large","status": 431,"title": "Request header fields exceed size limits"}"""
  }

  @Test
  fun `UnavailableForLegalReasons can be built and printed to JSON`() {
    val problem = Problem.UnavailableForLegalReasons()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/unavailable-for-legal-reasons","status": 451,"title": "Resource unavailable for legal reasons"}"""
  }
}
