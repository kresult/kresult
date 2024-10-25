# Module kresult-core

The core module contains all basic definitions and functionality, commonly used in KResult. Virtually all other modules
depend on this either implicitly or explicitly.

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

# Package io.kresult.core

The core package hosts the main entrypoint of the library: The **[KResult]** class. It provides builders, transformers
and extractors for result values and failures.