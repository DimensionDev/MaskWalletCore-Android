import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.google.protobuf.gradle.*

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.mozilla.rust-android-gradle.rust-android") version "0.8.6"
    id("com.google.protobuf") version "0.8.16"
    `maven-publish`
}

val protobufVersion = "3.17.0"

val maskWalletProtoSource = "$projectDir/src/main/rust/MaskWalletCore/chain-common/proto"
val maskWalletProtoTarget = "$buildDir/generated/proto"
val maskWalletProtoPackage = "com.dimension.maskwalletcore.proto"

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
                srcDir(maskWalletProtoTarget)
            }
        }
    }
}

dependencies {
    implementation("com.google.protobuf:protobuf-javalite:$protobufVersion")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

protobuf {
    protoc {
        artifact = if (osdetector.os == "osx") {
            "com.google.protobuf:protoc:$protobufVersion:osx-x86_64"
        } else {
            "com.google.protobuf:protoc:$protobufVersion"
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

tasks {
    val copyMaskProto by registering {
        doFirst {
            val source = File(maskWalletProtoSource)
            val target = File(maskWalletProtoTarget)
            source.copyRecursively(target, overwrite = true)
            updateProtoJavaPackage(target)
        }
    }
    val updateProtoVisibility by registering {
        doFirst {
            val source = File(protobuf.protobuf.generatedFilesBaseDir)
            updateProtoJavaVisibility(source)
        }
    }
    val protoClean by registering {
        delete(protobuf.protobuf.generatedFilesBaseDir)
        delete(maskWalletProtoTarget)
    }
    val cargoClean by registering(Exec::class) {
        executable = "cargo"
        args("clean")
        workingDir("$projectDir/${cargo.module}")
    }
    build.dependsOn(updateProtoVisibility)
}

fun updateProtoJavaVisibility(file: File) {
    if (file.isDirectory) {
        file.listFiles()?.forEach {
            updateProtoJavaVisibility(it)
        }
    } else {
        file.readText().replace("public final class", "final class").let {
            file.writeText(it)
        }
    }
}

fun updateProtoJavaPackage(file: File) {
    if (file.isDirectory) {
        file.listFiles()?.forEach {
            updateProtoJavaPackage(it)
        }
    } else {
        file.appendText("${System.lineSeparator()}option java_package = \"$maskWalletProtoPackage\";")
    }
}

afterEvaluate {
    tasks {
        val cargoBuild = named("cargoBuild")
        val copyMaskProto = named("copyMaskProto")
        val protoClean = named("protoClean")
        val cargoClean = named("cargoClean")

        preBuild.dependsOn(
            cargoBuild,
            copyMaskProto
        )
        clean.dependsOn(
            protoClean,
            cargoClean
        )
    }

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
