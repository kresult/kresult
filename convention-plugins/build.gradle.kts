plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  gradlePluginPortal()
}

dependencies{
  implementation(libs.plugindeps.vanniktech.mavenPublish)
  implementation(libs.plugindeps.dokka)
  implementation(libs.plugindeps.kotlinxKnit)
  implementation(libs.plugindeps.kotlinxKover)

  // https://github.com/gradle/gradle/issues/15383
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}