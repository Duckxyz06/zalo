plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.duckxyz.zgm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.duckxyz.zgm"
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "0.2.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "distribution"

    productFlavors {
        create("play") {
            dimension = "distribution"
            buildConfigField("String", "DISTRIBUTION_CHANNEL", "\"play\"")
            buildConfigField("boolean", "ENABLE_ACCESSIBILITY_AUTOMATION", "false")
            resValue("string", "app_name", "ZGM")
        }

        create("internal") {
            dimension = "distribution"
            applicationIdSuffix = ".internal"
            versionNameSuffix = "-internal"
            buildConfigField("String", "DISTRIBUTION_CHANNEL", "\"internal\"")
            buildConfigField("boolean", "ENABLE_ACCESSIBILITY_AUTOMATION", "true")
            resValue("string", "app_name", "ZGM Internal")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.01.01"))
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    val roomVersion = "2.8.5"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    testImplementation("junit:junit:4.13.2")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
