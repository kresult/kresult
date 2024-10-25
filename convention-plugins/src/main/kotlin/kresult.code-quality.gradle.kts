import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
  id("org.jlleitschuh.gradle.ktlint")
}

configure<KtlintExtension> {
  ignoreFailures.set(false)

  reporters {
    reporter(ReporterType.PLAIN)
  }
}