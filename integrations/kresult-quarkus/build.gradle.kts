plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotestMultiplatform)
  id("kresult.docs")
  id("kresult.maven-publish")
}

kotlin {
  jvm()
  linuxX64()

  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(project(":libs:kresult-problem"))
        implementation("io.quarkus.resteasy.reactive:resteasy-reactive-common:${libs.versions.quarkus.get()}")
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(libs.kotlin.test)

        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.framework.datatest)
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))

        implementation(libs.kotest.runner.junit5)
        implementation(libs.kotlinx.knit.test)
      }
    }
  }
}
