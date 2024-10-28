import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType

plugins {
  id("org.jetbrains.kotlin.multiplatform")
  id("io.kotest.multiplatform")
  id("org.jetbrains.kotlinx.kover")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()


tasks {
  withType<Test> {
    filter {
      isFailOnNoMatchingTests = false
    }
    testLogging {
      showExceptions = true
      showStandardStreams = true
      events = setOf(
        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
        org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
      )
      exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
  }

  findByName("jvmTest")?.let {
    named<Test>("jvmTest") {
      useJUnitPlatform()
    }
  }
}