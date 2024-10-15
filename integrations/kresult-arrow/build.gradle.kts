plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotestMultiplatform)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.dokka)
    id("module.publication")
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:versioning-plugin:${libs.versions.dokka.get()}")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":libs:kresult-core"))
                api(libs.arrow.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                val commonTest by getting {
                    dependencies {
                        implementation(libs.kotest.assertions.core)
                        implementation(libs.kotest.framework.engine)
                        implementation(libs.kotest.framework.datatest)
                        implementation(kotlin("test-common"))
                        implementation(kotlin("test-annotations-common"))
                    }
                }
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
