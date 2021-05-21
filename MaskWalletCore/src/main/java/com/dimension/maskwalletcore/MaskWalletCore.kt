package com.dimension.maskwalletcore

import com.dimension.maskwalletcore.proto.Api
import com.dimension.maskwalletcore.proto.Base
import com.dimension.maskwalletcore.proto.StoredKey


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

class StoredKey(
    private val storedKey: Base.StoredKeyInfo
) {
    companion object {
        fun fromMnemonic(value: String): com.dimension.maskwalletcore.StoredKey {
            val request = Api.MWRequest.newBuilder()
                .setParamImportMnemonic(
                    StoredKey.ImportMnemonicStoredKeyParam.newBuilder()
                        .setMnemonic(value)
                        .build()
                )
                .build()
                .toByteArray()
            val response = MaskWalletCore.request(request).let {
                Api.MWResponse.parseFrom(it)
            }.respImportMnemonic.storedKey
            return StoredKey(response)
        }

        fun fromJson(
            json: String,
            name: String,
            coinType: CoinType,
            password: String,
        ): com.dimension.maskwalletcore.StoredKey {
            val request = Api.MWRequest.newBuilder()
                .setParamImportJson(
                    StoredKey.ImportJSONStoredKeyParam.newBuilder()
                        .setJson(json)
                        .setPassword(password)
                        .setName(name)
                        .setCoin(coinType.toCoin())
                        .build()
                )
                .build()
                .toByteArray()

            val response = MaskWalletCore.request(request).let {
                Api.MWResponse.parseFrom(it)
            }.respImportJson.storedKey
            return StoredKey(response)
        }

        fun fromPrivateKey(
            privateKey: String,
            name: String,
            coinType: CoinType,
            password: String,
        ): com.dimension.maskwalletcore.StoredKey {
            val request = Api.MWRequest.newBuilder()
                .setParamImportPrivateKey(
                    StoredKey.ImportPrivateStoredKeyParam.newBuilder()
                        .setPrivateKey(privateKey)
                        .setPassword(password)
                        .setName(name)
                        .setCoin(coinType.toCoin())
                        .build()
                )
                .build()
                .toByteArray()
            val response = MaskWalletCore.request(request).let {
                Api.MWResponse.parseFrom(it)
            }.respImportPrivateKey.storedKey
            return StoredKey(response)
        }
    }
}