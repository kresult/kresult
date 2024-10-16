pluginManagement {
  includeBuild("convention-plugins")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "kresult"

// libs
include(":libs")
include(":libs:kresult-core")

// integrations
include(":integrations")
include(":integrations:kresult-arrow")

// other
include(":examples")