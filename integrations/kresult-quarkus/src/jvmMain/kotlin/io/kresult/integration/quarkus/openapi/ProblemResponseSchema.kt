package io.kresult.integration.quarkus.openapi

import io.kresult.problem.ProblemDefinition
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(
  name = "ProblemResponse",
  description = "Representation of a HTTP API Problem Details object, according to [RFC7807](https://www.rfc-editor.org/rfc/rfc7807)"
)
interface ProblemResponseSchema : ProblemDefinition {

  @get:Schema(
    description = "A URI reference [RFC3986](https://www.rfc-editor.org/rfc/rfc3986) that identifies the problem " +
      "type. This specification encourages that, when dereferenced, it provide human-readable documentation for the " +
      "problem type (e.g., using HTML " +
      "[W3C.REC-html5-20141028](https://www.rfc-editor.org/rfc/rfc7807#ref-W3C.REC-html5-20141028)). When this " +
      "member is not present, its value is assumed to be \"about:blank\"."
  )
  override val type: String

  @get:Schema(
    description = "A short, human-readable summary of the problem type.  It SHOULD NOT change from occurrence " +
      "to occurrence of the problem, except for purposes of localization (e.g., using proactive content negotiation; " +
      "see [RFC7231, Section 3.4](https://www.rfc-editor.org/rfc/rfc7231#section-3.4))."
  )
  override val title: String?

  @get:Schema(
    description = "The HTTP status code ([RFC7231, Section 6](https://www.rfc-editor.org/rfc/rfc7231#section-6)) " +
      "generated by the origin server for this occurrence of the problem."
  )
  override val status: Int

  @get:Schema(description = "A human-readable explanation specific to this occurrence of the problem.")
  override val detail: String?

  @get:Schema(
    description = "A URI reference that identifies the specific occurrence of the problem. It may or may not " +
      "yield further information if dereferenced."
  )
  override val instance: String?
}
