import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
  `maven-publish`
  signing
}

publishing {
  publications.withType<MavenPublication> {

    artifact(tasks.register("${name}JavadocJar", Jar::class) {
      archiveClassifier.set("javadoc")
      archiveAppendix.set(this@withType.name)
    })

    pom {
      name.set("KResult")
      description.set("An opinionated, functional Result type for Kotlin")
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
}

signing {
  if (project.hasProperty("signing.gnupg.keyName")) {
    useGpgCmd()
    sign(publishing.publications)
  }
}
