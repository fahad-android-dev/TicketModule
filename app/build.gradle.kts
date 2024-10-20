plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.orbits.ticketmodule"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.orbits.ticketmodule"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.websocket)
    implementation(libs.sdp)
    implementation(libs.ssp)
    implementation(libs.http)
    implementation(libs.httpUrl)
    implementation(libs.http)
    implementation(libs.nearpay)
    implementation(libs.socket)
    implementation(libs.datastore)
    implementation(libs.gson)
    implementation(libs.poi)
    implementation(libs.flexbox)
    implementation(libs.poi.xml)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.datastorePref)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}