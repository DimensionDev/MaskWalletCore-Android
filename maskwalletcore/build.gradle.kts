import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.google.protobuf.gradle.*
import java.util.Properties

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.mozilla.rust-android-gradle.rust-android") version "0.9.2"
    id("com.google.protobuf") version "0.8.18"
    `maven-publish`
    id("signing")
}

val protobufVersion = "3.20.0"

val maskWalletProtoSource = "$projectDir/src/main/rust/MaskWalletCore/chain-common/proto"
val maskWalletProtoTarget = "$buildDir/generated/proto"
val maskWalletProtoPackage = "com.dimension.maskwalletcore"

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    sourceSets {
        getByName("main") {
            proto {
                srcDir(maskWalletProtoTarget)
            }
        }
    }
    ndkVersion = "22.1.7171670"
}

// TODO: workaround for https://github.com/google/protobuf-gradle-plugin/issues/540
fun com.android.build.api.dsl.AndroidSourceSet.proto(action: SourceDirectorySet.() -> Unit) {
    (this as? ExtensionAware)
        ?.extensions
        ?.getByName("proto")
        ?.let { it as? SourceDirectorySet }
        ?.apply(action)
}

dependencies {
    implementation("com.google.protobuf:protobuf-kotlin-lite:$protobufVersion")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("kotlin") {
                    option("lite")
                }
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
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        dependsOn(updateProtoVisibility)
    }
    withType<JavaCompile> {
        dependsOn(updateProtoVisibility)
    }
    afterEvaluate {
        val cargoBuild = named("cargoBuild")
        preBuild.dependsOn(
            cargoBuild,
            copyMaskProto
        )
        clean.dependsOn(
            protoClean,
            cargoClean
        )
    }
}

fun updateProtoJavaVisibility(file: File) {
    if (file.isDirectory) {
        file.listFiles()?.forEach {
            updateProtoJavaVisibility(it)
        }
    } else {
        when (file.extension) {
            "java" -> file
                .readText()
                .replace("public final class", "final class")
                .let {
                    file.writeText(it)
                }
            "kt" -> file
                .readText()
                .replace("public inline fun", "internal inline fun")
                .replace("public object ", "internal object ")
                .replace("val com.dimension.maskwalletcore.", "internal val com.dimension.maskwalletcore.")
                .replace("internal internal", "internal")
                .let {
                    file.writeText(it)
                }
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

ext {
    val publishPropFile = rootProject.file("publish.properties")
    if (publishPropFile.exists()) {
        Properties().apply {
            load(publishPropFile.inputStream())
        }.forEach { name, value ->
            set(name.toString(), value)
        }
    } else {
        set("signing.keyId", System.getenv("SIGNING_KEY_ID"))
        set("signing.password", System.getenv("SIGNING_PASSWORD"))
        set("signing.secretKeyRingFile", System.getenv("SIGNING_SECRET_KEY_RING_FILE"))
        set("ossrhUsername", System.getenv("OSSRH_USERNAME"))
        set("ossrhPassword", System.getenv("OSSRH_PASSWORD"))
    }
}

publishing {
    signing {
        sign(publishing.publications)
    }
    repositories {
        maven {
            val releasesRepoUrl =
                "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl =
                "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = if (version.toString().endsWith("SNAPSHOT")) {
                uri(snapshotsRepoUrl)
            } else {
                uri(releasesRepoUrl)
            }
            credentials {
                username = project.ext.get("ossrhUsername").toString()
                password = project.ext.get("ossrhPassword").toString()
            }
        }
    }
    afterEvaluate {
        publications {
            create<MavenPublication>("release") {
                groupId = "io.github.dimensiondev"
                artifactId = "maskwalletcore"
                version = "0.5.0"

                pom {
                    name.set("MaskWalletCore")
                    description.set("MaskWalletCore Android port")
                    url.set("https://github.com/DimensionDev/MaskWalletCore-Android")

                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("Tlaster")
                            name.set("James Tlaster")
                            email.set("tlaster@outlook.com")
                        }
                    }
                    scm {
                        url.set("https://github.com/DimensionDev/MaskWalletCore-Android")
                        connection.set("scm:git:git://github.com/DimensionDev/MaskWalletCore-Android.git")
                        developerConnection.set("scm:git:git://github.com/DimensionDev/MaskWalletCore-Android.git")
                    }
                }

                from(components["release"])
            }
        }
    }
}
