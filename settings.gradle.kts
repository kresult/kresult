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
include(":kresult-core")
project(":kresult-core").projectDir = file("lib/kresult-core")

// integrations
include(":kresult-arrow")
project(":kresult-arrow").projectDir = file("integrations/kresult-arrow")

// other
include(":examples")