# MaskWallet-Android

# Requirement
## Rust target
```
rustup target add armv7-linux-androideabi   # for arm
rustup target add i686-linux-android        # for x86
rustup target add aarch64-linux-android     # for arm64
rustup target add x86_64-linux-android      # for x86_64
```
## Android toolchain
ndk

# Build guide
```
git clone --recurse-submodules git@github.com:DimensionDev/MaskWallet-Android.git
./gradlew :MaskWalletCore:build
```