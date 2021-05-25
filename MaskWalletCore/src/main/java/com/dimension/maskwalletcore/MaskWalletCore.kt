package com.dimension.maskwalletcore

internal object MaskWalletCore {
    init {
        System.loadLibrary("maskwalletdroid")
    }

    external fun request(value: ByteArray): ByteArray
}

enum class CoinType {
    Ethereum,
    Polkadot;

    internal fun toCoin() = when (this) {
        Ethereum -> Base.Coin.Ethereum
        Polkadot -> Base.Coin.Polkadot
    }
}

class PrivateKey private constructor(
    private val storedKey: Base.StoredKeyInfo
) {
    companion object {
        fun fromMnemonic(value: String): PrivateKey {
            val request = mWRequest {
                paramImportMnemonic = importMnemonicStoredKeyParam {
                    mnemonic = value
                }
            }.toByteArray()
            val response = MaskWalletCore.request(request).let {
                Api.MWResponse.parseFrom(it)
            }.respImportMnemonic.storedKey
            return PrivateKey(response)
        }

        fun fromJson(
            json: String,
            name: String,
            coinType: CoinType,
            password: String,
        ): PrivateKey {
            val request = mWRequest {
                paramImportJson = importJSONStoredKeyParam {
                    this.json = json
                    this.password = password
                    this.name = name
                    this.coin = coinType.toCoin()
                }
            }.toByteArray()
            val response = MaskWalletCore.request(request).let {
                Api.MWResponse.parseFrom(it)
            }.respImportJson.storedKey
            return PrivateKey(response)
        }

        fun fromPrivateKey(
            privateKey: String,
            name: String,
            coinType: CoinType,
            password: String,
        ): PrivateKey {
            val request = mWRequest {
                paramImportPrivateKey = importPrivateStoredKeyParam {
                    this.privateKey = privateKey
                    this.password = password
                    this.name = name
                    this.coin = coinType.toCoin()
                }
            }.toByteArray()
            val response = MaskWalletCore.request(request).let {
                Api.MWResponse.parseFrom(it)
            }.respImportPrivateKey.storedKey
            return PrivateKey(response)
        }
    }
}