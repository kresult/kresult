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
  * [Create Results](#create-results)
  * [Mapping & Transformation](#mapping-&-transformation)

<!--- END -->

## Features

- Support for full `map` and `flatMap` operations on both sides, success & failure
- Failure-side transformation and recovery
- Nullable (or not found) handling

## Usage

<!--- TEST_NAME ReadmeKnitTest -->

### Create Results

Creating `KResult` instances can be done using various methods. While there are explicit methods like using the
extension `Any.asSuccess()` or `Any.asFailure()`, results can be created from Kotlin Results, by catching exceptions or
from nullable types. Extensions with other frameworks provide creation methods as well. The following code block
showcases (some) creation methods:

```kotlin
import io.kotest.matchers.shouldBe
import io.kresult.core.KResult
import io.kresult.core.asKResult
import io.kresult.core.asSuccess

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
    .isFailure() shouldBe true

  // from nullable
  KResult
    .fromNullable(null) {
      RuntimeException("Value can not be null")
    }
    .isFailure() shouldBe true

  // from Kotlin Result
  Result.success("test")
    .asKResult()
    .isSuccess() shouldBe true
}
```

<!--- KNIT example-readme-01.kt -->
<!--- TEST lines.isEmpty() -->

### Mapping & Transformation

Kresults can be transformed in several ways. At the core, [KResult](TODO) implements functional mapping with `flatMap`,
`map`, `flatten` & `filter`:

```kotlin
import io.kotest.matchers.shouldBe
import io.kresult.core.KResult
import io.kresult.core.filter
import io.kresult.core.flatMap
import io.kresult.core.flatten

fun test() {
  val res: KResult<Throwable, String> = KResult.Success("test")

  // map
  res
    .map { "$it-1" }
    .getOrNull() shouldBe "test-1"

  // flatMap
  res
    .flatMap {
      if (it.length > 3) {
        KResult.Success(it)
      } else {
        KResult.Failure(RuntimeException("missing length"))
      }
    }
    .getOrNull() shouldBe "test"

  // flatten
  val nestedRes: KResult<Throwable, KResult<Throwable, String>> =
    KResult.Success(res)

  nestedRes.flatten().getOrNull() shouldBe "test"

  // filter
  res
    .filter(
      { it.isNotBlank() },
      { RuntimeException("String is empty") }
    )
    .isSuccess() shouldBe true
}
```

<!--- KNIT example-readme-02.kt -->
<!--- TEST lines.isEmpty() -->

Additional transformations, e.g. `merge`, `combine` and `swap` ca be used as well:

**TBD...**
