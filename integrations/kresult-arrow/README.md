# Module kresult-arrow

The [Arrow](integration) integration module provides functionality to integrate with the famous FP library for Kotlin, 
which was an inspiration for this project as well.

## Usage

**Gradle Kotlin DSL:**

```kotlin
dependencies {
  implementation("io.kresult.integration:kresult-arrow:VERSION")
}
```

**Gradle Groovy DSL:**

```groovy
implementation group: 'io.kresult.integration', name: 'kresult-arrow', version: 'VERSION'
```

**Maven:**

```xml
<dependency>
    <groupId>io.kresult.integration</groupId>
    <artifactId>kresult-arrow</artifactId>
    <version>VERSION</version>
</dependency>
```

# Package io.kresult.integration.arrow

Provides majorly transformation from and to Arrow types, e.g. 
[Either](https://apidocs.arrow-kt.io/arrow-core/arrow.core/-either/index.html).
