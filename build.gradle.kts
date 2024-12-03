// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // Remove Android Studio warning
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.devtools.ksp") version "1.9.21-1.0.15" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
        classpath("com.google.gms:google-services:4.4.2")
    }
}