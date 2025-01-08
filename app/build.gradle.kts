import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.jack.autostart"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jack.autostart"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        create("autoStartKey") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = file("../debug.keystore")
            storePassword = "android"
        }
    }
    buildTypes {
        applicationVariants.all {
            outputs.all {
                val versionName = defaultConfig.versionName
                val buildType = buildType.name
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val outputFileName = "AutoStart_${versionName}_${buildType}_${timestamp}.apk"
                (this as BaseVariantOutputImpl).outputFileName = outputFileName
                println("Apk output file name: $outputFileName")
            }
        }
        debug {
            signingConfig = signingConfigs.getByName("autoStartKey")
        }
        release {
            signingConfig = signingConfigs.getByName("autoStartKey")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}