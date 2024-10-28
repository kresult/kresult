import org.gradle.accessors.dm.LibrariesForLibs
import java.net.URI

plugins {
  id("org.jetbrains.dokka")
  id("org.jetbrains.kotlinx.knit")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

val dokkaPlugin by configurations
dependencies {
  dokkaPlugin("org.jetbrains.dokka:versioning-plugin:${libs.versions.dokka.get()}")
}

dokka {
  dokkaSourceSets {
    configureEach {
      if (File(projectDir, "README.md").canRead()) {
        includes.from("README.md")
      }

      sourceLink {
        localDirectory = rootDir
        remoteUrl = URI("https://github.com/kresult/kresult/tree/main")
        remoteLineSuffix = "#L"
      }
    }
  }
}

tasks {
  getByName("knitPrepare")
    .dependsOn(getTasksByName("dokka", true))
}