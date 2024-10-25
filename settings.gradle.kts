pluginManagement {
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

includeBuild("convention-plugins")

// libs
include(":libs")
include(":libs:kresult-core")
include(":libs:kresult-java")
include(":libs:kresult-problem")

// integrations
include(":integrations")
include(":integrations:kresult-arrow")

// other
include(":examples")
