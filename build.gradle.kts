import kotlinx.knit.KnitPluginExtension

plugins {
    id("root.publication")
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotestMultiplatform).apply(false)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.kotlinxKnit)
}

dependencies {
    dokka(project(":kresult-core"))
    dokka(project(":kresult-arrow"))

    kover(project(":kresult-core"))
    kover(project(":kresult-arrow"))
allprojects {
    group = "io.kresult"
    version = rootProject
        .file("version.txt")
        .readText()
        .trim()
}

configure<KnitPluginExtension> {
    siteRoot = "https://kresult.io/"
    rootDir = projectDir
    files = fileTree(projectDir) {
        include("**/*.md")
        include("**/*.kt")
        include("**/*.kts")

        exclude("**/build/**")
        exclude("**/.gradle/**")
    }
}

tasks {

    getByName("check").dependsOn(getTasksByName("knitCheck", true))
    getByName("knitPrepare").dependsOn(getTasksByName("dokka", true))
}

dokka {
    moduleName.set("KResult")
    dokkaPublicationDirectory.set(layout.projectDirectory.dir("docs/api"))
}