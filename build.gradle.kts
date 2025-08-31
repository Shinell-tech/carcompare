// <project-root>/build.gradle.kts

// Hilt 플러그인 태스크가 사용하는 클래스패스에 최신 javapoet을 주입
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.squareup:javapoet:1.13.0")
    }
}

// 플러그인은 여기서는 "적용만 비활성(apply false)" 하고
// 버전은 settings.gradle.kts의 pluginManagement에서만 관리합니다.
plugins {
    id("com.android.application") apply false
    id("org.jetbrains.kotlin.android") apply false
    id("com.google.devtools.ksp") apply false
    id("com.google.dagger.hilt.android") apply false
}
