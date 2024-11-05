package io.kresult.integration.quarkus

import io.kresult.core.KResult
import io.kresult.problem.Problem
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import jakarta.ws.rs.WebApplicationException
import kotlin.test.Test

class QuarkusExtensionsTest {

  @Test
  fun `should return 200 OK with value for Success`() {
    val result: KResult<Nothing, String> = KResult.Success("test-value")
    val response = result.toRestResponse()

    response.status shouldBe 200
    response.entity shouldBe "test-value"
  }

  @Test
  fun `should return 200 OK for Success unit`() {
    val result: KResult<Nothing, Unit> = KResult.Success.unit
    val response = result.toRestResponse()

    response.status shouldBe 200
    response.entity shouldBe Unit
  }

  @Test
  fun `should throw original exception for Throwable failure`() {
    val exception = RuntimeException("test-exception")
    val result: KResult<Throwable, String> = KResult.Failure(exception)

    val thrown = shouldThrow<RuntimeException> {
      result.toRestResponse()
    }
    thrown shouldBe exception
  }

  @Test
  fun `should create problem response for Problem failure`() {
    val problem = Problem.BadRequest(detail = "Invalid input")
    val result: KResult<Problem, String> = KResult.Failure(problem)

    val thrown = shouldThrow<WebApplicationException> {
      result.toRestResponse()
    }

    val response = thrown.response
    response.status shouldBe 400
    response.mediaType.toString() shouldBe "application/problem+json"
    response.entity.toString() shouldContain "Invalid input"
  }

  @Test
  fun `should create 500 response with string failure as detail`() {
    val errorMessage = "Something went wrong"
    val result: KResult<String, String> = KResult.Failure(errorMessage)

    val thrown = shouldThrow<WebApplicationException> {
      result.toRestResponse()
    }

    val response = thrown.response
    response.status shouldBe 500
    response.mediaType.toString() shouldBe "application/problem+json"
    response.entity.toString().let { entity ->
      entity shouldContain errorMessage
      entity shouldContain "\"status\": 500"
      entity shouldContain """"detail": "Something went wrong""""
    }
  }

  @Test
  fun `should create 500 response for non-string non-problem failure type`() {
    val result: KResult<Int, String> = KResult.Failure(42)

    val thrown = shouldThrow<WebApplicationException> {
      result.toRestResponse()
    }

    val response = thrown.response
    response.status shouldBe 500
    response.mediaType.toString() shouldBe "application/problem+json"
    response.entity.toString().let { entity ->
      entity shouldContain "An unknown error occurred"
      entity shouldContain "\"status\": 500"
    }
  }

  @Test
  fun `should handle FailureWithValue the same as Failure`() {
    val problem = Problem.BadRequest(detail = "Invalid input")
    val result: KResult<Problem, String> = KResult.FailureWithValue(problem, "original-value")

    val thrown = shouldThrow<WebApplicationException> {
      result.toRestResponse()
    }

    val response = thrown.response
    response.status shouldBe 400
    response.mediaType.toString() shouldBe "application/problem+json"
    response.entity.toString() shouldContain "Invalid input"
  }

  @Test
  fun `should handle string FailureWithValue as internal server error`() {
    val errorMessage = "Something went wrong"
    val result: KResult<String, Int> = KResult.FailureWithValue(errorMessage, 42)

    val thrown = shouldThrow<WebApplicationException> {
      result.toRestResponse()
    }

    val response = thrown.response
    response.status shouldBe 500
    response.mediaType.toString() shouldBe "application/problem+json"
    response.entity.toString().let { entity ->
      entity shouldContain errorMessage
      entity shouldContain "\"status\": 500"
    }
  }

  @Test
  fun `should handle complex success objects`() {
    data class ComplexObject(val id: Int, val name: String)
    val result: KResult<Nothing, ComplexObject> = KResult.Success(ComplexObject(1, "test"))

    val response = result.toRestResponse()

    response.status shouldBe 200
    response.entity.shouldBeInstanceOf<ComplexObject>()
    with(response.entity as ComplexObject) {
      id shouldBe 1
      name shouldBe "test"
    }
  }

  @Test
  fun `should set correct media type for Problem responses`() {
    val problem = Problem.BadRequest(detail = "Invalid input")
    val result: KResult<Problem, String> = KResult.Failure(problem)

    val thrown = shouldThrow<WebApplicationException> {
      result.toRestResponse()
    }

    val mediaType = thrown.response.mediaType
    mediaType.type shouldBe "application"
    mediaType.subtype shouldBe "problem+json"
    mediaType.toString() shouldBe "application/problem+json"
  }

  @Test
  fun `should preserve stack trace for Throwable failures`() {
    class CustomException(message: String) : Exception(message)
    val exception = CustomException("test-exception")
    val result: KResult<Throwable, String> = KResult.Failure(exception)

    val thrown = shouldThrow<CustomException> {
      result.toRestResponse()
    }

    thrown.stackTrace shouldBe exception.stackTrace
    thrown.message shouldBe exception.message
  }
}
