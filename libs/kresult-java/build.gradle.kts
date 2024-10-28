plugins {
  id("kresult.multiplatform")
  id("kresult.docs")
  id("kresult.maven-publish")
  id("kresult.code-quality")
}

kotlin {
  jvm()

  sourceSets {

    val jvmMain by getting {
      dependencies {
        implementation(project(":libs:kresult-core"))
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
