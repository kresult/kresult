buildscript {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    mavenLocal()
  }

  allprojects {
    version = rootProject
      .file("version.txt")
      .readText()
      .trim()
  }
}

plugins {
  base
  alias(libs.plugins.kotlinMultiplatform).apply(false)
  alias(libs.plugins.kotestMultiplatform).apply(false)
  alias(libs.plugins.sonarqube)
  alias(libs.plugins.kotlinxKover)
  id("kresult.docs-root")
}

dependencies {
  kover(project(":libs:kresult-core"))
  kover(project(":libs:kresult-java"))
  kover(project(":libs:kresult-problem"))
  kover(project(":integrations:kresult-arrow"))
  kover(project(":integrations:kresult-quarkus"))

  dokka(project(":libs:kresult-core"))
  dokka(project(":libs:kresult-java"))
  dokka(project(":libs:kresult-problem"))
  dokka(project(":integrations:kresult-arrow"))
  dokka(project(":integrations:kresult-quarkus"))
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
  rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().ignoreScripts = false
}

sonar {
  properties {
    property("sonar.projectKey", "kresult_kresult")
    property("sonar.organization", "kresult")
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/kover/report.xml")
  }
}
