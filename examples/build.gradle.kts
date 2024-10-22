plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotestMultiplatform)
  alias(libs.plugins.kotlinxKover)
}

group = "io.kresult"

kotlin {
  jvm()
  linuxX64()

  sourceSets {

    val commonTest by getting {
      dependencies {
        implementation(project(":libs:kresult-core"))
        implementation(project(":libs:kresult-java"))
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
