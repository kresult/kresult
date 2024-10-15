plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotestMultiplatform)
  alias(libs.plugins.dokka)
}

kotlin {
  jvm()

  sourceSets {

    val commonTest by getting {
      dependencies {
        implementation(project(":libs:kresult-core"))
        implementation(project(":integrations:kresult-arrow"))
        implementation(libs.kotlin.test)
        implementation(libs.arrow.core)
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(libs.kotest.runner.junit5)
        implementation(libs.kotlinx.knit.test)
      }
    }
  }
}

tasks.named<Test>("jvmTest") {
  useJUnitPlatform()
  testLogging {
    showExceptions = true
    showStandardStreams = true
    events = setOf(
      org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
      org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
    )
    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
  }
}
