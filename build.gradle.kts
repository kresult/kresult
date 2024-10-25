import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
version = rootProject
  .file("version.txt")
  .readText()
  .trim()

buildscript {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    mavenLocal()
  }
}

plugins {
  base
  alias(libs.plugins.dokka)
  alias(libs.plugins.kotlinxKover)
  alias(libs.plugins.kotlinMultiplatform).apply(false)
  alias(libs.plugins.kotestMultiplatform).apply(false)
  alias(libs.plugins.kotlinxKnit)
  alias(libs.plugins.sonarqube)
}

dependencies {
  kover(project(":libs:kresult-core"))
  kover(project(":libs:kresult-java"))
  kover(project(":libs:kresult-problem"))
  kover(project(":integrations:kresult-arrow"))
}

allprojects {
  version = rootProject
    .file("version.txt")
    .readText()
    .trim()

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
}

val docDir = layout.projectDirectory
  .dir("docs/api")

val docVersion = project.version.toString()
  .split(".")
  .let { parts ->
    if (parts.size == 3) {
      "${parts[0]}.${parts[1]}.X"
    } else {
      "INVALID-PROJECT-VERSION!"
    }
  }

tasks {

  withType<DokkaMultiModuleTask>().configureEach {
    val id = "org.jetbrains.dokka.versioning.VersioningPlugin"
    val config = """{ "version": "$docVersion", "olderVersionsDir":"$docDir" }"""
    val mapOf = mapOf(id to config)

    outputDirectory.set(docDir.dir(docVersion))
    pluginsMapConfiguration.set(mapOf)
  }
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
  rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().ignoreScripts = false
}

knit {
  siteRoot = "https://kresult.io/"
  rootDir = projectDir
  moduleRoots = listOf(".")
  moduleMarkers = listOf("build.gradle.kts")
  moduleDocs = docDir.toString()
  files = fileTree(projectDir) {
    include("**/*.md")
    include("**/*.kt")
    include("**/*.kts")

    exclude("**/build/**")
    exclude("**/.gradle/**")
  }
}

sonar {
  properties {
    property("sonar.projectKey", "kresult_kresult")
    property("sonar.organization", "kresult")
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/kover/report.xml")
  }
}