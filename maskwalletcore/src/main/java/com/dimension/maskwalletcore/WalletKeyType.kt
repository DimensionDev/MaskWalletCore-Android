package com.dimension.maskwalletcore

enum class WalletKeyType {
    PrivateKey,
    Mnemonic,
    UNRECOGNIZED,
}

internal fun Base.StoredKeyType.toKeyType(): WalletKeyType {
    return when (this) {
        Base.StoredKeyType.PrivateKey -> WalletKeyType.PrivateKey
        Base.StoredKeyType.Mnemonic -> WalletKeyType.Mnemonic
        Base.StoredKeyType.UNRECOGNIZED -> WalletKeyType.UNRECOGNIZED
    }
}
