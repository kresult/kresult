# Module kresult-java

Provides interoperability helpers to integrate with Java libraries.

**Note:** *KResult is NOT a Java library! While `kresult-java` provides helpers for Java interoperability, it remains a
library for Kotlin to be used with Kotlin projects. The intent of providing Java interoperability is to make KResult
usable with Java types (e.g. from legacy libs) in Kotlin projects and not vice versa.*

## Usage

**Gradle Kotlin DSL:**

```kotlin
dependencies {
  implementation("io.kresult:kresult-java:VERSION")
}
```

**Gradle Groovy DSL:**

```groovy
implementation group: 'io.kresult', name: 'kresult-java', version: 'VERSION'
```

**Maven:**

```xml
<dependency>
    <groupId>io.kresult</groupId>
    <artifactId>kresult-java</artifactId>
    <version>VERSION</version>
</dependency>
```

# Package io.kresult.java

Provides specific extensions for Java types, e.g. `java.util.Optional`. See api docs for details.
