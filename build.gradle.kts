// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.42.0")
        classpath("gradle.plugin.com.onesignal:onesignal-gradle-plugin:0.14.0")
        // other dependencies
    }
    repositories {
        mavenCentral()
    }
}
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.android.library") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.github.ben-manes.versions") version "0.42.0" apply false

}
allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}