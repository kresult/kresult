plugins {
  id("kresult.multiplatform")
  id("kresult.docs")
  id("kresult.maven-publish")
  id("kresult.code-quality")
}

kotlin {
  jvm()

  sourceSets {
    val jvmMain by getting {
      dependencies {
        api(project(":libs:kresult-core"))
        implementation(project(":libs:kresult-problem"))

        implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${libs.versions.quarkus.get()}"))

        implementation("io.quarkus.resteasy.reactive:resteasy-reactive-common")
        implementation("org.eclipse.microprofile.openapi:microprofile-openapi-api:3.1.1")
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(libs.kotlin.test)

        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.framework.datatest)
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))

        implementation(libs.kotest.runner.junit5)
        implementation(libs.kotlinx.knit.test)

        // quarkus
        implementation("io.quarkus:quarkus-rest")
        implementation("io.quarkus:quarkus-smallrye-openapi")
        implementation("io.quarkus:quarkus-rest-jackson")
        implementation("io.quarkus:quarkus-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("io.quarkus:quarkus-arc")
      }
    }
  }
}
