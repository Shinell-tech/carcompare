plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")        // ✔ 여기서 적용
    id("com.google.devtools.ksp") // ✔ 여기서 적용(버전은 루트에서 가져옴)
}


android {
    namespace = "com.shinhyeong.carcompare"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.shinhyeong.carcompare"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures { viewBinding = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }
}

dependencies {
    // Room
    implementation("com.google.code.gson:gson:2.13.1") // 또는 2.11.0
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.room:room-runtime:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")

    // Paging3
    implementation("androidx.paging:paging-runtime:3.3.6")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.3")

    // Kotlinx Serialization (초기 seed/로컬 동기화용)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}