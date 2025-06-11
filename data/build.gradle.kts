plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.dluong.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    buildFeatures {
        buildConfig = true
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
    implementation(projects.domain)
    implementation(projects.designsystem)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    // Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.timber) {
        exclude(group = "com.intellij", module = "annotations")
    }
    // Testing
    testImplementation(libs.junit)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit + OkHttp Bundle
    implementation(libs.bundles.retrofit.okhttp3) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "com.intellij", module = "annotations")
    }

    // Moshi Bundle
    implementation(libs.bundles.moshi) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
        exclude(group = "com.intellij", module = "annotations")
    }

    // Ktor Bundle - Comment out nếu không sử dụng
    implementation(libs.bundles.ktor) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
        exclude(group = "com.intellij", module = "annotations")
    }

}