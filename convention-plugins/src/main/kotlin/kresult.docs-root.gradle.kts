import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask

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

val docDir = rootDir.resolve("docs/api")

val docVersionsDir = docDir.resolve("versions")

val docVersion = project.version.toString()
  .split(".")
  .let { parts ->
    if (parts.size == 3) {
      "${parts[0]}.${parts[1]}.X"
    } else {
      "INVALID-PROJECT-VERSION!"
    }
  }

val versionedOutputDir = docVersionsDir.resolve(docVersion)
val publicOutputDir = docDir.resolve("public")

dokka {

  dokkaPublications.html {
    outputDirectory.set(versionedOutputDir)

    includes.from("README.md")
  }

  pluginsConfiguration.html {
    footerMessage = "KResult APi Documentation - [docs.kresult.io](https://docs.kresult.io)"
  }

  pluginsConfiguration.versioning {
    version = docVersion
    olderVersionsDir = docVersionsDir
    renderVersionsNavigationOnAllPages = true
  }
}

tasks {

  withType<DokkaGeneratePublicationTask>().configureEach {
    doFirst {
      logger.lifecycle("Deleting output dir: $versionedOutputDir")
      versionedOutputDir.deleteRecursively()
    }

    doLast {
      logger.lifecycle("Copying output dir to public")
      versionedOutputDir.copyRecursively(publicOutputDir, true)
      logger.lifecycle("Removing 'older' versioned output")
      versionedOutputDir.resolve("older").deleteRecursively()
    }
  }
}

knit {
  siteRoot = "https://kresult.io/"
  rootDir = projectDir
  moduleRoots = listOf(".")
  moduleMarkers = listOf("build.gradle.kts")
  moduleDocs = docDir.toString()
  files = fileTree(projectDir) {
    include("**/*.md")
    include("**/*.kt")
    include("**/*.kts")

    exclude("**/build/**")
    exclude("**/.gradle/**")
  }
}