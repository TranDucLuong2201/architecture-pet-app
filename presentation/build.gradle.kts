plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.dluong.presentation"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
kotlin {
    jvmToolchain(11)
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(projects.data)
    implementation(projects.designsystem)
    implementation(projects.domain)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.bundles.androidx.navigation)
    // Image Loading - Coil Bundle
    implementation(libs.bundles.coil) {
        exclude(group = "com.intellij", module = "annotations")
    }    // Material 3 Additional
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.adaptive.navigation.android)
    implementation("com.google.accompanist:accompanist-swiperefresh:0.32.0")
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dagger Hilt Bundle
    implementation(libs.bundles.dagger.hilt) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "com.intellij", module = "annotations")
    }
    ksp(libs.dagger.hilt.compiler)
}