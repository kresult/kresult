# Module kresult-quarkus

The [Quarkus](https://quarkus.io/) integration module provides functionality to integrate with
[Quarkus REST](https://quarkus.io/extensions/io.quarkus/quarkus-rest/) and
[SmallRye OpenAPI](https://quarkus.io/extensions/io.quarkus/quarkus-smallrye-openapi/).

*The following example is just here to showcase the integration. A complete (and fully tested) example can be found at
**[github.com/kresult/example-quarkus](https://github.com/kresult/example-quarkus)**.*

### Returning a Response from `KResult<E, T>`

Given a response of type:

```kotlin
@Schema(description = "The User response object")
data class User(val id: UUID, val name: String)
```

`KResult<E, T>` can be used to indicate success or failure of the action. The final result is transformed to a quarkus
REST response using [
`toRestResponse`](https://kresult.io/integrations/kresult-quarkus/io.kresult.integration.quarkus/to-rest-response.html):

```kotlin
@Path("/demo")
class DemoResource {

  @GET
  fun success(): RestResponse<User> =
    KResult
      .Success(
        User(id = UUID.randomUUID(), name = "Jane Doe")
      )
      .toRestResponse()
}
```

The snippet above responds with a JSON representation of the user:

```js
{
  "id"
:
  "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "name"
:
  "string"
}
```

**Heads Up:** Make sure you either have a [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/)
or (with problem support) a [Problem](https://kresult.io/libs/kresult-problem/io.kresult.problem/-problem/) /
[ProblemDefinition](https://kresult.io/libs/kresult-problem/io.kresult.problem/-problem-definition/) of the failure side
of the returned `KResult`. Otherwise, the failure-case response will always result in an **Internal Server Error**, as
we don't know what to respond for any other type.

### Problem Support

Additionally, [kresult-problem](https://kresult.io/libs/kresult-problem/) can be used for Problem JSON support:

```kotlin
@Path("/demo")
class DemoResource {

  @GET
  fun success(): RestResponse<User> =
    KResult
      .Failure(
        Problem.NotFound(detail = "User with that ID could not be found")
      )
      .toRestResponse()
}
```

The output of the snippet above responds with an [RFC7807](https://datatracker.ietf.org/doc/html/rfc7807) compliant
HTTP Problem Response of type `application/problem+json`:

```js
{
  "type"
:
  "https://kresult.io/problem/not-found",
    "status"
:
  404,
    "title"
:
  "The requested resource was not found",
    "detail"
:
  "User with that ID could not be found"
}
```

**Heads Up**: This needs the `kresult-problem` module as well.

For additional details, see [kresult-problem](https://kresult.io/libs/kresult-problem/) module documentation.

### OpenAPI Annotations

If [problem support](#problem-support) is used together with
[SmallRye OpenAPI](https://quarkus.io/extensions/io.quarkus/quarkus-smallrye-openapi/), failure API responses can and
should be annotated. If you use KResults included problems, e.g. `Problem.NotFound`, the following annotations can be
used:

```kotlin
import io.kresult.integration.quarkus.openapi.BadRequestApiResponse

@Path("/demo")
class DemoResource {

  @GET
  @BadRequestApiResponse
  fun success(): RestResponse<User> =
    KResult
      .Failure(
        Problem.BadRequest()
      )
      .toRestResponse()
}
```

This will add a response code, description and schema definition like the following:

![Bad Request OpenAPI Response](https://kresult.io/images/assets/screenshots/bad-request-openapi-ui.png)

However, `BadRequestApiResponse` is just a shorthand, you could make your own if you want. See
[io/kresult/integration/quarkus/openapi/Annotations.kt](https://github.com/kresult/kresult/tree/main/integrations/kresult-quarkus/src/jvmMain/kotlin/io/kresult/integration/quarkus/openapi/Annotations.kt)
for details.

## Usage

**Gradle Kotlin DSL:**

```kotlin
dependencies {
}
```

**Gradle Groovy DSL:**

```groovy
```

**Maven:**

```xml
<dependency>
    <groupId>io.kresult.integration</groupId>
    <version>VERSION</version>
</dependency>
```

# Package io.kresult.integration.arrow

Provides majorly transformation from and to Arrow types, e.g.
[Either](https://apidocs.arrow-kt.io/arrow-core/arrow.core/-either/index.html).
