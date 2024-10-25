import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost

plugins {
  id("com.vanniktech.maven.publish")
}

// hack to get version, as project.version does not exist at this stage
val version = rootProject.version.toString()

configure<MavenPublishBaseExtension> {
  publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
  signAllPublications()
  coordinates(group.toString(), project.name, version)
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