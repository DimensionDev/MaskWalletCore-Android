package com.dimension.maskwalletcore

enum class CoinType {
    Ethereum,
    Polkadot;

    internal fun toCoin() = when (this) {
        Ethereum -> Base.Coin.Ethereum
        Polkadot -> Base.Coin.Polkadot
    }
}