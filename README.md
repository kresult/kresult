# KResult

[![Maven Central Version](https://img.shields.io/maven-central/v/io.kresult/kresult-core)](https://central.sonatype.com/namespace/io.kresult)
[![Kotlin version](https://img.shields.io/badge/Kotlin-2.0.20-blue)](https://kotlinlang.org/docs/whatsnew2020.html)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

[![Build Status](https://img.shields.io/github/actions/workflow/status/kresult/kresult/build.yml?branch=main&logo=github)](https://github.com/kresult/kresult/actions/workflows/build.yml?query=branch%3Amain+)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=kresult_kresult&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=kresult_kresult)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=kresult_kresult&metric=coverage)](https://sonarcloud.io/summary/new_code?id=kresult_kresult)

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
  * [Core Modules](#core-modules)
  * [Integrations](#integrations)
* [Hello, World](#hello-world)
* [License](#license)

<!--- END -->

## Features

- Support for full `map` and `flatMap` operations on both sides, success & failure
- Failure-side transformation and recovery
- Nullable (or not found) handling

## Usage

[KResult](https://kresult.io) is hosted on Maven Central. Use your favorite build tool to add a dependency. Replace
`VERSION` with the current version number:

**Using Gradle Kotlin DSL:**

```kotlin
dependencies {
  implementation("io.kresult:kresult-core:VERSION")
}
```

**Using Gradle Groovy DSL:**

```groovy
implementation group: 'io.kresult', name: 'kresult-core', version: 'VERSION'
```

**Using Maven:**

```xml

<dependency>
  <groupId>io.kresult</groupId>
  <artifactId>kresult-core</artifactId>
  <version>VERSION</version>
</dependency>
```

### Core Modules

| Module              | Description                                                                                                  |
|---------------------|--------------------------------------------------------------------------------------------------------------|
| [kresult-core]()    | Core module providing the functional `KResult` type as well as tooling and core functionality around it      |
| [kresult-java]()    | Java interoperability for `KResult` and its transformations.                                                 |
| [kresult-problem]() | [RFC7807](https://datatracker.ietf.org/doc/html/rfc7807) compliant Problem Details JSON support for KResult. |

### Integrations

| Module            | Description                                                                      |
|-------------------|----------------------------------------------------------------------------------|
| [kresult-arrow]() | Supports seamless integration with the [Arrow](https://arrow-kt.io/) FP library. |

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
import io.kresult.core.*
import io.kotest.matchers.shouldBe

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
      !it.name.contains("/")
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
  response.content shouldBe "Hello, World!"
}
```

<!--- KNIT example-readme-02.kt -->
<!--- TEST lines.isEmpty() -->

## License

Copyright 2024 [kresult.io](https://kresult.io) authors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
