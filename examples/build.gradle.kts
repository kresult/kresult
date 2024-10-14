plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotestMultiplatform)
    alias(libs.plugins.dokka)
}

kotlin {
    jvm()

    sourceSets {
        val jvmTest by getting {
            dependencies {
                implementation(project(":kresult-core"))
                implementation(project(":kresult-arrow"))
                implementation(libs.kotest.runner.junit5)
                implementation(libs.kotlinx.knit.test)
                implementation(libs.arrow.core)
            }
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
    filter {
        isFailOnNoMatchingTests = false
    }
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
