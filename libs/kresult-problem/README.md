# Module kresult-problem

Provides [RFC7807](https://datatracker.ietf.org/doc/html/rfc7807) compliant Problem JSON support

<!--- TEST_NAME ProblemReadmeKnitTest -->

By using a `Problem` on the `Failure` side of a `KResult`, strongly typed HTTP problems rendering to
`application/json+problem` can be utilized:

```kotlin
import io.kresult.core.KResult
import io.kresult.problem.Problem
import io.kotest.matchers.shouldBe

fun test() {
  val res = KResult.Failure(
    Problem.NotFound(detail = "User with that ID could not be found")
  )

  res.onFailure {
    println(it.toJson(pretty = true))
  }

  res.error.status shouldBe 404
}
```

<!--- KNIT example-problemreadme-01.kt -->
<!--- TEST lines.isEmpty() -->
<!--- CLEAR -->

The result looks like:

```js
// application/json+problem
{
  "type": "https://kresult.io/problem/not-found",
  "status": 404,
  "title": "The requested resource was not found",
  "detail": "User with that ID could not be found"
}
```

## Usage

**Gradle Kotlin DSL:**

```kotlin
dependencies {
  implementation("io.kresult:kresult-problem:VERSION")
}
```

**Gradle Groovy DSL:**

```groovy
implementation group: 'io.kresult', name: 'kresult-problem', version: 'VERSION'
```

**Maven:**

```xml
<dependency>
    <groupId>io.kresult</groupId>
    <artifactId>kresult-problem</artifactId>
    <version>VERSION</version>
</dependency>
```

# Package io.kresult.problem

Provides `ProblemDefinition` and implementations of it in `Problem`.
