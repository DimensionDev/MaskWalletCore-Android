pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.toString() == "org.mozilla.rust-android-gradle.rust-android") {
                useModule("com.github.Tlaster:rust-android-gradle:-SNAPSHOT")

            }
        }
    }
    repositories {
        maven("https://jitpack.io")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}


rootProject.name = "MaskWallet"
include(":sample")
include(":MaskWalletCore")
