package com.dimension.maskwalletcore

data class CreateKeyResult(
    val key: WalletKey,
    val mnemonic: String,
)