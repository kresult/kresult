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
        api(project(":libs:kresult-core"))
        implementation(project(":libs:kresult-problem"))

        implementation("io.quarkus.resteasy.reactive:resteasy-reactive-common:${libs.versions.quarkus.get()}")
        implementation("org.eclipse.microprofile.openapi:microprofile-openapi-api:3.1.1")
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
