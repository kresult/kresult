# KResult

[![CI Build](https://github.com/kresult/kresult/actions/workflows/build.yml/badge.svg?branch=develop)](https://github.com/kresult/kresult/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**A functional Result Type for Kotlin**

*[KResult](https://kresult.io) is a functional library that provides an opinionated and feature-rich extension / 
alternative to Kotlin's [Result\<T\>](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/) type. While the core 
library is as lean and dependency-free as possible, seamless integrations with common frameworks are provided via 
integration packages or plugins.*

KResult is heavily inspired by:
* Scala's [Try\[T\]](https://www.scala-lang.org/api/current/scala/util/Try.html) type
* Rust's [Result\<T, E\>](https://doc.rust-lang.org/std/result/enum.Result.html).
* Arrow's [Either\<A, B\>](https://apidocs.arrow-kt.io/arrow-core/arrow.core/-either/index.html)

## Features
- Support for full `map` and `flatMap` operations on both sides, success & failure
- Failure-side transformation and recovery
- Nullable (or not found) handling