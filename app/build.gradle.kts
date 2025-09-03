plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.example.pizzeria"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication2"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)                 // ui-core
    implementation(libs.androidx.ui.graphics)         // ui-graphics
    implementation(libs.androidx.ui.tooling.preview) // ui-preview
    implementation(libs.androidx.material3)           // Material3
    implementation(libs.androidx.compose.foundation) // Foundation
    implementation(libs.androidx.compose.material)   // Material2 (иконки и компоненты)

    // Navigation
    implementation(libs.androidx.navigation.compose) // navigation-compose

    // Классический Android‑UI (если нужны)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)         // Material Design View‑based
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.viewmodel)

    // Coil для картинок (Compose)
    implementation(libs.coil.compose)

    // Activity‑Compose
    implementation(libs.androidx.activity.compose)

    // Тесты
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
