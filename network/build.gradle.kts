plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.dluong.network"
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
    // Networking
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

    implementation(libs.bundles.retrofit.okhttp3) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "com.intellij", module = "annotations")
    }
    // RxJava
    implementation(libs.rxjava) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.rxandroid)

    implementation(libs.lottie)
    implementation(libs.flowExt)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    coreLibraryDesugaring(libs.desugar.jdk.libs)


    implementation(libs.hilt.navigation.compose)
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
}