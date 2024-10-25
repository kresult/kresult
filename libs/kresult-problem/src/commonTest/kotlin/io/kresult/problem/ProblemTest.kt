import io.kresult.problem.Problem
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ProblemTest {

  @Test
  fun `can be built and printed to JSON`() {
    Problem.NotFound()
      .toJson(pretty = false) shouldBe """{"type": "https://kresult.io/problem/not-found","status": 404,"title": "The requested resource was not found"}"""
  }

  @Test
  fun `default toJson does not pretty-print`() {
    Problem.NotFound()
      .toJson() shouldBe """{"type": "https://kresult.io/problem/not-found","status": 404,"title": "The requested resource was not found"}"""
  }

  @Test
  fun `can be pretty-printed to JSON`() {
    Problem.NotFound()
      .toJson(pretty = true) shouldBe """
      {
       "type": "https://kresult.io/problem/not-found",
       "status": 404,
       "title": "The requested resource was not found"
      }
    """.trimIndent().trim()
  }

  @Test
  fun `detail is added to json if not null`() {
    Problem.NotFound(detail = "An extended problem description stating some details about the occurrence and context")
      .toJson(pretty = true) shouldBe """
      {
       "type": "https://kresult.io/problem/not-found",
       "status": 404,
       "title": "The requested resource was not found",
       "detail": "An extended problem description stating some details about the occurrence and context"
      }
    """.trimIndent().trim()
  }

  @Test
  fun `instance is added to json if not null`() {
    Problem.NotFound(instance = "https://kresult.io/tests/instance/6f5f572d-99cf-4206-b2b7-40d099401ec0")
      .toJson(pretty = true) shouldBe """
      {
       "type": "https://kresult.io/problem/not-found",
       "status": 404,
       "title": "The requested resource was not found",
       "instance": "https://kresult.io/tests/instance/6f5f572d-99cf-4206-b2b7-40d099401ec0"
      }
    """.trimIndent().trim()
  }
}
