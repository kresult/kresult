# KResult

**A functional Result Type for Kotlin**

*[KResult](https://kresult.io) is a functional library that provides an opinionated and feature-rich extension / 
alternative to Kotlin's [Result\<T\>](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/) type. While the core 
library is as lean and dependency-free as possible, seamless integrations with common frameworks are provided via 
integration packages or plugins. KResult is heavily inspired by Scala's 
[Try\[T\]](https://www.scala-lang.org/api/current/scala/util/Try.html) type and Rust's 
[Result\<T, E\>](https://doc.rust-lang.org/std/result/enum.Result.html).*

Addressed challenges:
- Support for `flatMap` operations on results
- Failure-side mapping (transformation of one failure to another failure)
- Nullable (or not found) handling