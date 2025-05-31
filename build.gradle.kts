// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.gradle.spotless) apply false
    alias(libs.plugins.kotlin.serialization) apply false

}

allprojects {
    afterEvaluate {
        extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension>()?.run {
            jvmToolchain {
                languageVersion = JavaLanguageVersion.of(21)
                vendor = JvmVendorSpec.AZUL
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
            freeCompilerArgs = listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlin.ExperimentalStdlibApi",
                "-opt-in=kotlin.time.ExperimentalTime",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )
        }
    }
    val ktlintVersion = rootProject.libs.versions.ktlint.get()
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude(
                // Compose Multiplatform Res class
                "**/Res.kt",
                // Kotlin generated files
                "**/build/**/*.kt",
            )

            ktlint(ktlintVersion)

            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
        kotlinGradle {
            target("**/*.gradle.kts", "*.gradle.kts")
            targetExclude("**/build/**/*.kts")

            ktlint(ktlintVersion)

            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
    }
}