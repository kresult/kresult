# KResult

[![Maven Central Version](https://img.shields.io/maven-central/v/io.kresult/kresult-core)](https://central.sonatype.com/namespace/io.kresult)
[![Kotlin version](https://img.shields.io/badge/Kotlin-2.0.20-blue)](https://kotlinlang.org/docs/whatsnew2020.html)

[![CI Build](https://github.com/kresult/kresult/actions/workflows/build.yml/badge.svg?branch=develop)](https://github.com/kresult/kresult/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**An opinionated, functional Result type for Kotlin**

*[KResult](https://kresult.io) is a functional library that provides an opinionated and feature-rich extension /
alternative to Kotlin's [Result\<T\>](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/) type. While the core
library is as lean and dependency-free as possible, seamless integrations with common frameworks are provided via
integration packages or plugins. KResult is heavily inspired by
Scala's [Try\[T\]](https://www.scala-lang.org/api/current/scala/util/Try.html) type,
Rust's [Result\<T, E\>](https://doc.rust-lang.org/std/result/enum.Result.html) and
Arrow's [Either\<A, B\>](https://apidocs.arrow-kt.io/arrow-core/arrow.core/-either/index.html).*

<!--- TOC -->

* [Features](#features)
* [Usage](#usage)
* [Hello, World](#hello-world)

<!--- END -->

## Features

- Support for full `map` and `flatMap` operations on both sides, success & failure
- Failure-side transformation and recovery
- Nullable (or not found) handling

## Usage

[KResult](https://kresult.io) is hosted on Maven Central. Use your favorite build tool to add a dependency. Replace 
`VERSION` with the current version number:

**Using Gradle Kotlin**

```kotlin
dependencies {
  implementation("io.kresult:kresult-core:VERSION")
}
```

**Using Gradle Groovy DSL**

```groovy
implementation group: 'io.kresult', name: 'kresult-core', version: 'VERSION'
```

**Using Maven**

```xml
<dependency>
    <groupId>io.kresult</groupId>
    <artifactId>kresult-core</artifactId>
    <version>VERSION</version>
</dependency>
```

## Hello, World

<!--- CLEAR -->
<!--- TEST_NAME ReadmeKnitTest -->

```kotlin
import io.kresult.core.KResult
import io.kresult.core.filter

fun test() {
  val greeting = KResult.Success("World")

  val res: KResult<String, String> = greeting
    // transform result, if successful
    .map {
      """Hello, $it!"""
    }
    // transform to IllegalArgumentException on failure side, if value is blank
    .filter(IllegalArgumentException("Greeting should not be blank")) {
      it.isNotBlank()
    }
    // execute side-effect, if successful
    .onSuccess {
      print("log: $it is successful")
    }
    // transform failure from Exception to a string
    .mapFailure {
      "${it.javaClass}: ${it.localizedMessage}"
    }

  // fold result to string
  res.fold(
    { error -> "Result failed with error: $error" },
    { value -> "Result was successful with value: $value" },
  )
}
```
<!--- KNIT example-readme-01.kt -->
<!--- TEST lines.isEmpty() -->

While strings are simple for showcasing, a more real world solution would have strongly typed result values. One common 
practice is to encode:

* `Success` side value as a product type (`data class` in Kotlin)
* `Failure` side value as a sum type / tagged union (`enum` or `sealed class` in Kotlin)

The following example demonstrates a very simplified case, where a greeting message is validated for correctness, and 
the resulting `KResult<Failure, Greeting>` is folded to an exemplary `Response` object again. In a more practical setup, 
that `Response` and the mechanics to produce it would of course be provided by a framework integration:

```kotlin
import io.kotest.matchers.shouldBe
import io.kresult.core.*

// Product to encode success
data class Greeting(val name: String)

// SUm to encode a failure
sealed class Failure(val msg: String) {

  // Indicates that Failure was caused by invalid input
  interface InputValidationProblem
  data object BlankName : Failure("Name should not be blank"), InputValidationProblem
  data object IllegalCharacters : Failure("Name contains illegal characters"), InputValidationProblem

  data object Unknown : Failure("An unknown error occured")
}

// Product to encode a response
data class Response(val status: Int, val content: String)

fun test() {
  val greeting: KResult<Failure, Greeting> = Greeting("World")
    // Wraps any type into a KResult.Success
    .asSuccess()
    // Filter, transforming to failure if greeting name is blank
    .filter(Failure.BlankName) {
      it.name.isNotBlank()
    }
    .filter(Failure.IllegalCharacters) {
      it.name.contains("/")
    }

  val response: Response = greeting
    // Transforms Success side to a Response
    .map {
      Response(status = 200, content = """Hello, ${it.name}!""")
    }
    // Transforms Failure side to a Response
    .mapFailure { failure ->
      when (failure) {
        is Failure.InputValidationProblem ->
          Response(status = 400, content = failure.msg)

        else ->
          Response(status = 500, content = failure.msg)
      }
    }
    // When Success and Failure side are of the same type, we can merge them (long syntax for fold)
    .merge()

  response.status shouldBe 200
  response.content shouldBe "Hello, World"
}
```
<!--- KNIT example-readme-02.kt -->
<!--- TEST lines.isEmpty() -->
