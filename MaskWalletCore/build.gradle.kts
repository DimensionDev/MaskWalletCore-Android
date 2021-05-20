import com.google.protobuf.gradle.*

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.mozilla.rust-android-gradle.rust-android") version "0.8.6"
    id("com.google.protobuf") version "0.8.16"
    `maven-publish`
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
    sourceSets {
        getByName("main") {
            proto {
                srcDir("src/main/rust/MaskWalletCore/chain-common/proto")
            }
        }
    }
}

dependencies {
    implementation("com.google.protobuf:protobuf-javalite:3.17.0")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

protobuf {
    protoc {
        artifact = if (osdetector.os == "osx") {
            "com.google.protobuf:protoc:3.17.0:osx-x86_64"
        } else {
            "com.google.protobuf:protoc:3.17.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

cargo {
    module = "./src/main/rust/wrapper"
    targets = listOf("arm", "x86", "arm64", "x86_64")
    libname = "maskwalletdroid"
    profile = "release"
}

task<Exec>("cargoClean") {
    executable = "cargo"
    args("clean")
    workingDir("$projectDir/${cargo.module}")
}

task("protoClean") {
    delete(protobuf.protobuf.generatedFilesBaseDir)
}

tasks.getByName("preBuild").dependsOn("cargoBuild")
tasks.getByName("clean").dependsOn("cargoClean", "protoClean")


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.dimension.maskwallet"
                artifactId = "maskwalletcore"
                version = "0.1.0"

                from(components["release"])
            }
        }
    }
}