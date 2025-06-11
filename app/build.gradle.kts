plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.gradle.spotless) apply false
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.gms.google.services)

}


android {
    namespace = "com.dluong.pet"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dluong.pet"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Remove the javaCompileOptions block - not needed for KSP
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    flavorDimensions.add("default")
    productFlavors {
        create("dev") {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "Pet Dev")
            buildConfigField("String", "API_DOMAIN", "\"https://api.thecatapi.com/v1/\"")
            buildConfigField(
                "String",
                "API_KEY",
                "\"live_g64F7G1r4JPDKLYJdj5wcR6pyOeQyATPoNQpNPeWLZ15IW4bFgL9bVgmuex2ZfMn\""
            )
        }
        create("product") {
            applicationIdSuffix = ".product"
            versionNameSuffix = "-product"
            resValue("string", "app_name", "Pet")
            buildConfigField(
                "String",
                "API_DOMAIN",
                "\"https://api.thecatapi.com/v1/\""
            )
            buildConfigField(
                "String",
                "API_KEY",
                "\"live_g64F7G1r4JPDKLYJdj5wcR6pyOeQyATPoNQpNPeWLZ15IW4bFgL9bVgmuex2ZfMn\""
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // Giải quyết packaging conflicts
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/gradle/incremental.annotation.processors"
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/license.txt"
            excludes += "/META-INF/NOTICE"
            excludes += "/META-INF/NOTICE.txt"
            excludes += "/META-INF/notice.txt"
            excludes += "/META-INF/ASL2.0"
            excludes += "/META-INF/*.kotlin_module"
            excludes += "/META-INF/annotations.kotlin_module"
            // Fix Room compiler processing license conflict
            excludes += "/META-INF/androidx/room/room-compiler-processing/LICENSE.txt"
        }
    }
}

// Configure KSP for Room schema export
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

// Giải quyết dependency resolution conflicts
configurations.all {
    resolutionStrategy {
        // Force sử dụng version mới nhất của annotations
        force("org.jetbrains:annotations:23.0.0")
        force("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${libs.versions.kotlin.get()}")

        // Exclude các versions cũ gây conflict
        exclude(group = "com.intellij", module = "annotations")
    }
}

dependencies {
    implementation(projects.data)
    implementation(projects.designsystem)
    implementation(projects.domain)
    implementation(projects.presentation)
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.kotlinx.coroutines.play.services) // hoặc bản mới hơn

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI Bundle
    implementation(libs.bundles.androidx.compose.ui)

    // Lifecycle & ViewModel Bundle
    implementation(libs.bundles.androidx.lifecycle)

    // Navigation Bundle
    implementation(libs.bundles.androidx.navigation)

    // Room Database Bundle
    implementation(libs.bundles.androidx.room) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.hilt.common)
    implementation(libs.firebase.firestore.ktx)
    ksp(libs.androidx.room.compiler)

    // Dagger Hilt Bundle
    implementation(libs.bundles.dagger.hilt) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "com.intellij", module = "annotations")
    }
    ksp(libs.dagger.hilt.compiler)

    // Coroutines Bundle
    implementation(libs.bundles.kotlinx.coroutines)

    // Media3 Bundle
    implementation(libs.bundles.media3) {
        exclude(group = "androidx.compose.ui", module = "ui")
        exclude(group = "androidx.compose.ui", module = "ui-graphics")
        exclude(group = "com.intellij", module = "annotations")
    }

    // Image Loading - Coil Bundle
    implementation(libs.bundles.coil) {
        exclude(group = "com.intellij", module = "annotations")
    }
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // RxJava
    implementation(libs.rxjava) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.rxandroid)

    // Utilities
    implementation(libs.timber) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.glide) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.lottie)
    implementation(libs.flowExt)

    // Material 3 Additional
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.adaptive.navigation.android)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.androidx.ui.testing)
    debugImplementation(libs.androidx.ui.tooling)
    androidTestImplementation(libs.androidx.work.testing)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt Testing
    androidTestImplementation(libs.dagger.hilt.test)
    kspAndroidTest(libs.dagger.hilt.compiler)
}

// Remove the RoomSchemaArgProvider class - not needed for KSP