package io.kresult.problem

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ProblemTest5xx {

  @Test
  fun `InternalServerError can be built and printed to JSON`() {
    val problem = Problem.InternalServerError()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/internal-server-error","status": 500,"title": "An unexpected error occurred while processing the request"}"""
  }

  @Test
  fun `NotImplemented can be built and printed to JSON`() {
    val problem = Problem.NotImplemented()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/not-implemented","status": 501,"title": "The requested functionality is not implemented"}"""
  }

  @Test
  fun `BadGateway can be built and printed to JSON`() {
    val problem = Problem.BadGateway()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/bad-gateway","status": 502,"title": "Invalid response received from upstream server"}"""
  }

  @Test
  fun `ServiceUnavailable can be built and printed to JSON`() {
    val problem = Problem.ServiceUnavailable()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/service-unavailable","status": 503,"title": "The service is temporarily unavailable"}"""
  }

  @Test
  fun `GatewayTimeout can be built and printed to JSON`() {
    val problem = Problem.GatewayTimeout()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/gateway-timeout","status": 504,"title": "Upstream server response timed out"}"""
  }

  @Test
  fun `HttpVersionNotSupported can be built and printed to JSON`() {
    val problem = Problem.HttpVersionNotSupported()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/http-version-not-supported","status": 505,"title": "HTTP version used in the request is not supported"}"""
  }

  @Test
  fun `VariantAlsoNegotiates can be built and printed to JSON`() {
    val problem = Problem.VariantAlsoNegotiates()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/variant-also-negotiates","status": 506,"title": "Server detected an internal configuration error"}"""
  }

  @Test
  fun `InsufficientStorage can be built and printed to JSON`() {
    val problem = Problem.InsufficientStorage()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/insufficient-storage","status": 507,"title": "Server has insufficient storage to complete the request"}"""
  }

  @Test
  fun `LoopDetected can be built and printed to JSON`() {
    val problem = Problem.LoopDetected()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/loop-detected","status": 508,"title": "Server detected an infinite loop while processing the request"}"""
  }

  @Test
  fun `NotExtended can be built and printed to JSON`() {
    val problem = Problem.NotExtended()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/not-extended","status": 510,"title": "Further extensions to the request are required"}"""
  }

  @Test
  fun `NetworkAuthenticationRequired can be built and printed to JSON`() {
    val problem = Problem.NetworkAuthenticationRequired()
    problem.toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/network-authentication-required","status": 511,"title": "Network authentication is required to access the resource"}"""
  }
}
