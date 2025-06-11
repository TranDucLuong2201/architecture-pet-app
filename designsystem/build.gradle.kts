plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.dluong.designsystem"
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
    implementation(libs.timber) {
        exclude(group = "com.intellij", module = "annotations")
    }
    // Ktor Bundle - Comment out nếu không sử dụng
    implementation(libs.bundles.ktor) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
        exclude(group = "com.intellij", module = "annotations")
    }
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
    // Lifecycle & ViewModel Bundle
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}