import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "io.rm.log4j2.android.sample"
        minSdkVersion(16)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    androidExtensions {
        configure(delegateClosureOf<AndroidExtensionsExtension> {
            isExperimental = true
        })
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":log4j2_android"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.11")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.core:core-ktx:1.1.0-alpha03")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("org.apache.logging.log4j:log4j-api:2.3")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1")
}