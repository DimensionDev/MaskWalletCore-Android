buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")
    }
}

plugins {
    id("org.gradle.android.cache-fix") version "2.3.1" apply false
}
subprojects {
    repositories {
        google()
        mavenCentral()
    }
    plugins.withType<com.android.build.gradle.BasePlugin>() {
        apply(plugin = "org.gradle.android.cache-fix")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
