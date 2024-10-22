import com.vanniktech.maven.publish.SonatypeHost

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotestMultiplatform)
  alias(libs.plugins.kotlinxKover)
  alias(libs.plugins.vanniktech.mavenPublish)
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
