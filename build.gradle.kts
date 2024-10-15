import kotlinx.knit.KnitPluginExtension

plugins {
    id("root.publication")
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotestMultiplatform).apply(false)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.kotlinxKnit)
}

dependencies {
    dokka(project(":libs:kresult-core"))
    dokka(project(":integrations:kresult-arrow"))

    kover(project(":libs:kresult-core"))
    kover(project(":integrations:kresult-arrow"))
}

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