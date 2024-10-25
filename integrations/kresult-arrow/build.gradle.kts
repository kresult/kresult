import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

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
        implementation(project(":libs:kresult-core"))
        api(libs.arrow.core)
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

  @Suppress("DEPRECATION")
  tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets {
      configureEach {
        externalDocumentationLink {
          url.set(URL("https://apidocs.arrow-kt.io/"))
          packageListUrl.set(URL("https://apidocs.arrow-kt.io/package-list"))
        }
      }
    }
  }
}
