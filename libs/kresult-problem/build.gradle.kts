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

    val commonMain by getting {
      dependencies {
        api(project(":libs:kresult-core"))
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.framework.datatest)
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
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
