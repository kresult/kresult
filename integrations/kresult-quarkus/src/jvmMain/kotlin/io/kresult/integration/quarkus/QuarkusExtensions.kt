package io.kresult.integration.quarkus

import io.kresult.core.KResult
import io.kresult.problem.Problem
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder

private val PROBLEM_JSON = MediaType("application", "problem+json")

/**
 * Converts a KResult to a RestResponse, handling different error types appropriately.
 *
 * @return A RestResponse containing either the success value or an appropriate error response
 * @throws WebApplicationException with appropriate status code and problem details
 */
fun <E, T> KResult<E, T>.toRestResponse(): RestResponse<T> =
  fold(
    ifFailure = { failure ->
      throw when (failure) {
        is Throwable -> failure
        is Problem -> createProblemException(failure)
        is String -> createProblemException(
          Problem.InternalServerError(detail = failure)
        )
        else -> createUnknownErrorException()
      }
    },
    ifSuccess = { success -> ResponseBuilder.ok(success).build() }
  )

private fun createProblemException(problem: Problem): WebApplicationException =
  WebApplicationException(
    ResponseBuilder
      .create<String>(problem.status)
      .entity(problem.toJson())
      .type(PROBLEM_JSON)
      .build()
      .toResponse()
  )

private fun createUnknownErrorException(): WebApplicationException =
  WebApplicationException(
    ResponseBuilder
      .create<String>(500)
      .entity(
        Problem
          .InternalServerError(detail = "An unknown error occurred and it could not be mapped.")
          .toJson()
      )
      .type(PROBLEM_JSON)
      .build()
      .toResponse()
  )
