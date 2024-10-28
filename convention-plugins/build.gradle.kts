plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  gradlePluginPortal()
}

dependencies{
  implementation(libs.plugindeps.mavenPublish)
  implementation(libs.plugindeps.dokka)
  implementation(libs.plugindeps.kotlinMultiplatform)
  implementation(libs.plugindeps.kotestMultiplatform)
  implementation(libs.plugindeps.kotlinxKnit)
  implementation(libs.plugindeps.kotlinxKover)
  implementation(libs.plugindeps.ktlint)

  // https://github.com/gradle/gradle/issues/15383
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}