import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask

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

  buildscript {
    dependencies {
      classpath("org.jetbrains.dokka:versioning-plugin:1.9.20")
    }
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
  alias(libs.plugins.ktlint)
}

dependencies {
  kover(project(":libs:kresult-core"))
  kover(project(":libs:kresult-java"))
  kover(project(":libs:kresult-problem"))
  kover(project(":integrations:kresult-arrow"))

  val dokkaPlugin by configurations
  dependencies {
    dokkaPlugin("org.jetbrains.dokka:versioning-plugin:${libs.versions.dokka.get()}")
  }

  dokka(project(":libs:kresult-core"))
  dokka(project(":libs:kresult-java"))
  dokka(project(":libs:kresult-problem"))
  dokka(project(":integrations:kresult-arrow"))
}

allprojects {

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

val docDir = rootDir.resolve("docs/api")

val docVersionsDir = docDir.resolve("versions")

val docVersion = project.version.toString()
  .split(".")
  .let { parts ->
    if (parts.size == 3) {
      "${parts[0]}.${parts[1]}.X"
    } else {
      "INVALID-PROJECT-VERSION!"
    }
  }

val versionedOutputDir = docVersionsDir.resolve(docVersion)
val publicOutputDir = docDir.resolve("public")

dokka {

  dokkaPublications.html {
    outputDirectory.set(versionedOutputDir)

    includes.from("README.md")
  }

  pluginsConfiguration.html {
    footerMessage = "KResult APi Documentation - [docs.kresult.io](https://docs.kresult.io)"
  }

  pluginsConfiguration.versioning {
    version = docVersion
    olderVersionsDir = docVersionsDir
    renderVersionsNavigationOnAllPages = true
  }
}

tasks {

  withType<DokkaGeneratePublicationTask>().configureEach {
    doFirst {
      logger.lifecycle("Deleting output dir: $versionedOutputDir")
      versionedOutputDir.deleteRecursively()
    }

    doLast {
      logger.lifecycle("Copying output dir to public")
      versionedOutputDir.copyRecursively(publicOutputDir, true)
      logger.lifecycle("Removing 'older' versioned output")
      versionedOutputDir.resolve("older").deleteRecursively()
    }
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
