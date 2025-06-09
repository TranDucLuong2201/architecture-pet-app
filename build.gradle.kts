import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.gradle.spotless) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    id("com.github.ben-manes.versions") version "0.46.0"
    id("nl.littlerobots.version-catalog-update") version "0.8.0"
}

allprojects {
    // Apply spotless to all projects including root (if root has Kotlin files)
    plugins.withId("com.diffplug.spotless") {
        configure<com.diffplug.gradle.spotless.SpotlessExtension> {
            val ktlintVersion = rootProject.libs.versions.ktlint.get()
            kotlin {
                target("**/*.kt")
                targetExclude(
                    "**/Res.kt",
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

    afterEvaluate {
        extensions.findByType<KotlinBaseExtension>()?.let {
            it.jvmToolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
                vendor.set(JvmVendorSpec.AZUL)
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            freeCompilerArgs.set(
                listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlin.ExperimentalStdlibApi",
                    "-opt-in=kotlin.time.ExperimentalTime",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview"
                )
            )
        }
    }
}

subprojects {
    // Apply spotless plugin to all subprojects
    apply(plugin = "com.diffplug.spotless")

    // Apply versions plugin to subprojects to check dependency updates
    apply(plugin = "com.github.ben-manes.versions")
}

versionCatalogUpdate {
    sortByKey.set(true)
    keep {
        keepUnusedVersions.set(true)
        keepUnusedLibraries.set(true)
        keepUnusedPlugins.set(true)
    }
}

// Reject non-stable versions for dependency updates
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = Regex("^[0-9,.v-]+(-r)?$")
    return !stableKeyword && !regex.matches(version)
}

tasks.withType<DependencyUpdatesTask>().configureEach {
    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Release candidate")
                }
            }
        }
    }
}
