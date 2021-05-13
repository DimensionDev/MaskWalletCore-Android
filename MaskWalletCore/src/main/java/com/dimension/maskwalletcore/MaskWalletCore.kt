package com.dimension.maskwalletcore

internal object MaskWalletCore {
    init {
        System.loadLibrary("maskwalletdroid")
    }
    external fun request(value: ByteArray): ByteArray
}