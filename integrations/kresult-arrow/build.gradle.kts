import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotestMultiplatform)
  alias(libs.plugins.kotlinxKover)
  alias(libs.plugins.dokka)
  alias(libs.plugins.vanniktech.mavenPublish)
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
        val commonTest by getting {
          dependencies {
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.framework.engine)
            implementation(libs.kotest.framework.datatest)
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
          }
        }
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

mavenPublishing {
  publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

  signAllPublications()

  coordinates(group.toString(), project.name, version.toString())

  pom {
    name.set("KResult")
    description.set("An opinionated, functional Result type for Kotlin")
    inceptionYear.set("2024")
    url.set("https://kresult.io")

    licenses {
      license {
        name.set("MIT")
        url.set("https://opensource.org/licenses/MIT")
      }
    }

    developers {
      developer {
        id.set("frne")
        name.set("Frank Neff")
        url.set("https://frankneff.com")
      }
      developer {
        id.set("kresult-contributors")
        name.set("KResult Contributors")
        url.set("https://github.com/kresult/kresult/graphs/contributors")
      }
    }

    issueManagement {
      system.set("GitHub Issues")
      url.set("https://github.com/kresult/kresult/issues")
    }

    scm {
      url.set("https://github.com/kresult/kresult")
    }
  }
}