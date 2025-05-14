plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.gradle.spotless) apply false

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

        defaultConfig {
            javaCompileOptions {
                annotationProcessorOptions {
                    compilerArgumentProviders(
                        RoomSchemaArgProvider(
                            File(projectDir, "schemas").apply {
                                if (!exists()) {
                                    check(mkdirs()) { "Cannot create $this" }
                                }
                            },
                        ),
                    )
                }
            }
        }
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


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.startup.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.bundles.retrofit.okhttp3)
    implementation(libs.bundles.moshi)
    implementation(libs.timber)
    implementation(libs.glide)
    implementation(libs.lottie)
    implementation(libs.flowExt)

    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.bundles.kotlinx.coroutines)
    implementation(libs.coil.compose)
}
class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File,
) : CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> {
        // Note: If you're using KSP, change the line below to return
        // listOf("room.schemaLocation=${schemaDir.path}").
        return listOf("-Aroom.schemaLocation=${schemaDir.path}")
    }
}
