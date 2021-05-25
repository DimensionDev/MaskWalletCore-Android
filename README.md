# MaskWalletCore-Android
![Build Status](https://github.com/DimensionDev/MaskWalletCore-Android/workflows/Android/badge.svg)


# Development

## Requirement
- Rust target
```
rustup target add armv7-linux-androideabi i686-linux-android aarch64-linux-android x86_64-linux-android
```
- Android NDK

## Build guide
```
git clone --recurse-submodules git@github.com:DimensionDev/MaskWalletCore-Android.git
./gradlew :MaskWalletCore:build
```