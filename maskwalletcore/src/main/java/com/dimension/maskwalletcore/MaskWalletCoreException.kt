package com.dimension.maskwalletcore

class MaskWalletCoreException internal constructor(
    val code: String?,
    message: String?
) : Exception(message)