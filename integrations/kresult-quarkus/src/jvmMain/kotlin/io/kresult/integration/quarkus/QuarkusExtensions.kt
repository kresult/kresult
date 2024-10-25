package io.kresult.integration.quarkus

import io.kresult.core.KResult
import io.kresult.problem.Problem
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder

fun <E, T> KResult<E, T>.toRestResponse(): RestResponse<T> =
  fold(
    { failure ->
      when (failure) {
        is Throwable ->
          throw failure

        is Problem ->
          throw WebApplicationException(
            ResponseBuilder
              .create<String>(failure.status)
              .entity(failure.toJson())
              .type(MediaType("application", "problem+json"))
              .build()
              .toResponse(),
          )

        else -> throw WebApplicationException(
          ResponseBuilder
            .create<String>(500)
            .entity(
              Problem
                .InternalServerError(detail = "An unknown error occured and it could not be mapped.")
                .toJson(),
            )
            .type(MediaType("application", "problem+json"))
            .build()
            .toResponse(),
        )
      }
    },
    { success ->
      ResponseBuilder
        .ok(success)
        .build()
    },
  )
