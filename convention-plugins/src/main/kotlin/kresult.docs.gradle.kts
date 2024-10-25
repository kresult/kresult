import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  id("org.jetbrains.dokka")
  id("org.jetbrains.kotlinx.knit")
  id("org.jetbrains.kotlinx.kover")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

val dokkaPlugin by configurations
dependencies {
  dokkaPlugin("org.jetbrains.dokka:versioning-plugin:${libs.versions.dokka.get()}")
}

tasks {
  withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets {
      configureEach {
        if (File(projectDir, "README.md").canRead()) {
          includes.from("README.md")
        }

        sourceLink {
          localDirectory.set(rootDir)
          remoteUrl.set(URL("https://github.com/kresult/kresult/tree/main"))
          remoteLineSuffix.set("#L")
        }
      }
    }
  }

  getByName("knitPrepare")
    .dependsOn(getTasksByName("dokka", true))
}