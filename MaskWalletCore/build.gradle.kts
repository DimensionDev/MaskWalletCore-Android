plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.mozilla.rust-android-gradle.rust-android") version "0.8.6"
    id("com.google.protobuf") version "0.8.16"
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

cargo {
    module = "./src/main/rust"
    targets = listOf("arm", "x86", "arm64", "x86_64")
    libname = "maskwalletdroid"
    profile = "release"
}

task<Exec>("cargoClean") {
    executable = "cargo"
    args("clean")
    workingDir("$projectDir/${cargo.module}")
}

tasks.getByName("preBuild").dependsOn("cargoBuild")
tasks.getByName("clean").dependsOn("cargoClean")
