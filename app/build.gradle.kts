plugins {
    id("com.android.application")          // ← 버전 표기 제거
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
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

// ---- versions ----
val roomVersion = "2.7.2"
val hiltVersion = "2.57.1"
val pagingVersion = "3.3.6"
val workVersion = "2.10.3"
val serializationJson = "1.9.0"
val materialVersion = "1.12.0"

dependencies {
    // UI & utils
    implementation("com.google.android.material:material:$materialVersion")
    implementation("com.google.code.gson:gson:2.13.1")

    // Room (KSP)
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Hilt (KAPT)
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Paging / WorkManager
    implementation("androidx.paging:paging-runtime:$pagingVersion")
    implementation("androidx.work:work-runtime-ktx:$workVersion")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationJson")
}

kapt {
    correctErrorTypes = true
}

// Room 스키마 출력(경고 제거)
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.generateKotlin", "true")
}

// Hilt 실행 시 낮은 javapoet 끼어드는 문제 방지
configurations.configureEach {
    resolutionStrategy.eachDependency {
        if (requested.group == "com.squareup" && requested.name == "javapoet") {
            useVersion("1.13.0")
            because("Hilt가 필요로 하는 ClassName.canonicalName()은 1.12.1+에 존재")
        }
    }
}
