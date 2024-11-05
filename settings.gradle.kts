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
include(
  ":libs",
  ":libs:kresult-core",
  ":libs:kresult-java",
  ":libs:kresult-problem",
)

// integrations
include(
  ":integrations",
  ":integrations:kresult-arrow",
  ":integrations:kresult-quarkus",
)

// other
include(":examples")
