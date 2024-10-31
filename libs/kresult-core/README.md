# Module kresult-core

The core module contains all basic definitions and functionality, commonly used in KResult. Virtually all other modules
depend on this either implicitly or
explicitly. [KResult](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/) and its
inheritors [Success](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/-success/), [Failure](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/-failure/)
and [FailureWithValue](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/-failure-with-value/) provide an
opinionated,
functional result type. While Kotlin has
its own [kotlin.Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/) already, the intent
of [KResult](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/) is to be more
functional, usable and better integrated.

## Usage

*Note: Literally all integrations and extensions depend on `kresult-core`, so if any other `kresult-*` dependency is in
place, this is not needed.*

**Gradle Kotlin DSL:**

```kotlin
dependencies {
  implementation("io.kresult:kresult-core:VERSION")
}
```

**Gradle Groovy DSL:**

```groovy
implementation group: 'io.kresult', name: 'kresult-core', version: 'VERSION'
```

**Maven:**

```xml

<dependency>
  <groupId>io.kresult</groupId>
  <artifactId>kresult-core</artifactId>
  <version>VERSION</version>
</dependency>
```

## Creating Results

<!--- CLEAR -->
<!--- TEST_NAME CoreReadmeKnitTest -->

Results can be created by
constructing [Success](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/-success/)
or [Failure](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/-failure/) directly, but there is a variety
of helpers and
third
party types to create results from:

```kotlin
import io.kresult.core.KResult
import io.kresult.core.asKResult
import io.kresult.core.asSuccess
import io.kotest.matchers.shouldBe

fun test() {
  // by instance
  KResult.Success("test")
    .isSuccess() shouldBe true

  // by extension function
  "test".asSuccess()
    .isSuccess() shouldBe true

  // by catching exceptions
  KResult
    .catch {
      throw RuntimeException("throws")
    }
    .isSuccess() shouldBe false

  // from nullable
  KResult
    .fromNullable(null) {
      RuntimeException("Value can not be null")
    }
    .isSuccess() shouldBe false

  // from Kotlin Result
  Result.success("test")
    .asKResult()
    .isSuccess() shouldBe true
}
```

<!--- KNIT example-core-01.kt -->
<!--- TEST lines.isEmpty() -->

### From Extensions

There are more result builders with extensions, e.g. from [kresult-arrow](https://kresult.io/libs/kresult-arrow).

## Mapping & Transformation

Results can be transformed in several ways. At the
core, [KResult](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/)
implements functional mapping with `flatMap`,
`map`, `flatten`, `filter`, etc. Some of these are extension functions that need to be imported.:

```kotlin
import io.kresult.core.KResult
import io.kresult.core.filter
import io.kresult.core.flatMap
import io.kresult.core.flatten
import io.kotest.matchers.shouldBe

fun test() {
  // map
  KResult.Success(3)
    .map { it - 1 }
    .getOrNull() shouldBe 2

  // flatMap
  KResult.Success("some-p4ss!")
    .flatMap {
      if (it.length > 8) {
        KResult.Success(it)
      } else {
        KResult.Failure(RuntimeException("Password is too short"))
      }
    } shouldBe KResult.Success("some-p4ss!")

  // filter
  KResult.Success("some-p4ss!")
    .filter(RuntimeException("String is empty")) {
      it.isNotBlank()
    } shouldBe KResult.Success("some-p4ss!")

  // flatten
  KResult.Success(KResult.Success(2))
    .flatten() shouldBe KResult.Success(2)
}
```

<!--- KNIT example-core-02.kt -->
<!--- TEST lines.isEmpty() -->

## Getting values or errors

As a [KResult](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/) is always either a `Success` or a
`Failure` /
`FailureWithValue`, a simple, kotlin-native `when`
expression or a destructuring declaration can be used to get the `Success.value` or `Failure.error`:

**Heads up:** If you destructure
a [FailureWithValue](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/-failure-with-value/), you'll get an
`error` **AND** a
`value` back!

```kotlin
import io.kresult.core.KResult
import io.kresult.core.KResult.Failure
import io.kresult.core.KResult.FailureWithValue
import io.kresult.core.KResult.Success
import io.kotest.matchers.shouldBe

fun test() {
  val res: KResult<String, Int> = Success(2)

  // when expression
  when (res) {
    is Success -> "success: ${res.value}"
    is Failure -> "failure: ${res.error}"
    is FailureWithValue -> "failure: ${res.error} (original value was ${res.value})"
  } shouldBe "success: 2"


  // destructuring call
  val (error, value) = res
  error shouldBe null
  value shouldBe 2
}
```

<!--- KNIT example-core-03.kt -->
<!--- TEST lines.isEmpty() -->

### Convenience Methods

Additionally, convenience methods like `fold`, `getOrNull`, `getOrDefault`, or `getOrThrow` for results that have a
`Throwable` on failure side.

```kotlin
import io.kresult.core.KResult
import io.kresult.core.getOrDefault
import io.kresult.core.getOrThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

fun test() {
  // fold
  KResult.Success(2).fold(
    { "failure: $it" },
    { "success: $it" }
  ) shouldBe "success: 2"

  // getOrNull
  KResult.Success(2).getOrNull() shouldBe 2
  KResult.Failure("error").getOrNull() shouldBe null

  // getOrElse
  KResult.Success(2).getOrDefault { -1 } shouldBe 2
  KResult.Failure("error").getOrDefault { -1 } shouldBe -1

  // getOrThrow
  KResult.Success(2).getOrThrow() shouldBe 2
  shouldThrow<RuntimeException> {
    KResult.Failure(RuntimeException("test")).getOrThrow()
  }
}
```

<!--- KNIT example-core-04.kt -->
<!--- TEST lines.isEmpty() -->

## Multiple Failures or Success values

Returning a **list of successful values** with a result, as well as indicating **multiple failure reasons** are
common cases. If we are, for example **validating user input**, we want to indicate all validations that failed,
not just the first.

Because of the flexibility of [KResult](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/), this can
easily be achieved by
just representing the [Success](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/-success/)
or [Failure](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/-failure/)
side with a `List` respectively. Examples could be `KResult<List<Error>, String>` or
`KResult<List<Error>, List<String>>` if we have multiple success results as well. As these type definition can become
rather complex, predefined typealiases can be used:

- [MultiErrorKResult](https://kresult.io/libs/kresult-core/io.kresult.core/-multi-error-k-result/) if failure side
  carries a list
- [MultiValueKResult](https://kresult.io/libs/kresult-core/io.kresult.core/-multi-value-k-result/) if success side
  carries a list
  To make working with success or failure lists easier, there are a few conveninet helpers. Keep in mind that the
  `KResult.validate` extension can only be used with `MultiErrorKResult` or more generally, if you have a `List` on
  the [Failure](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/-failure/) side:

```kotlin
import io.kresult.core.KResult
import io.kresult.core.MultiErrorKResult
import io.kresult.core.errors
import io.kresult.core.validate
import io.kotest.matchers.shouldBe

data class User(val name: String, val age: Int, val active: Boolean)

enum class ValidationError(val message: String) {
  InvalidName("Name must not be empty"),
  IllegalAge("User must be over 18 years old"),
  Inactive("User must active"),
}

fun test() {
  val user: MultiErrorKResult<ValidationError, User> =
    KResult.Success(User(name = "Max", age = 17, active = false))

  val result: MultiErrorKResult<ValidationError, User> = user
    .validate(ValidationError.InvalidName) { u ->
      u.name.isNotBlank()
    }
    .validate(ValidationError.IllegalAge) { u ->
      u.age >= 18
    }
    .validate(ValidationError.Inactive) { u ->
      u.active
    }

  result.isFailure() shouldBe true

  result.errors().size shouldBe 2
  result.errors()[0] shouldBe ValidationError.IllegalAge
  result.errors()[1] shouldBe ValidationError.Inactive
}
```

<!--- KNIT example-core-05.kt -->
<!--- TEST lines.isEmpty() -->

# Package io.kresult.core

The core package hosts the main entrypoint of the library: The *
*[KResult](https://kresult.io/libs/kresult-core/io.kresult.core/-k-result/)** class. It provides builders, transformers
and extractors for result values and failures.
