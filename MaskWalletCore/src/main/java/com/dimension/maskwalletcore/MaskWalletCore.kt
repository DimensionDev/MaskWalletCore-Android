package com.dimension.maskwalletcore

internal object MaskWalletCore {
    init {
        System.loadLibrary("maskwalletdroid")
    }
    external fun test(value: String): String
}