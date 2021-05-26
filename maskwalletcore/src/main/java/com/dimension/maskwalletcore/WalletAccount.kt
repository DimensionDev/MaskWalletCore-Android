package com.dimension.maskwalletcore

class WalletAccount internal constructor(
    private val account: Base.StoredKeyAccountInfo
) {
    val address: String
        get() = account.address
    val name: String
        get() = account.name
    val coin: String
        get() = account.coin
    val derivationPath: String
        get() = account.derivationPath
    val extendedPublicKey: String
        get() = account.extendedPublicKey
}