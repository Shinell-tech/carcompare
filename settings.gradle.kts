pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        val kotlinVersion = "2.2.10"
        val agpVersion    = "8.12.2"
        val kspVersion    = "2.2.10-2.0.2"
        val hiltGradle    = "2.57.1"

        id("com.android.application") version agpVersion
        id("org.jetbrains.kotlin.android") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
        id("org.jetbrains.kotlin.kapt") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.parcelize") version kotlinVersion
        id("com.google.devtools.ksp") version kspVersion
        id("com.google.dagger.hilt.android") version hiltGradle
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}

rootProject.name = "carcompare"
include(":app")
