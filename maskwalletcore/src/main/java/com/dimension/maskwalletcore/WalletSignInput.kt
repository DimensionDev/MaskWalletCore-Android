package com.dimension.maskwalletcore

sealed interface WalletSignInput

data class EthereumSignInput(
    val amount: String,
    val chainId: Long,
    val gasLimit: String,
    val gasPrice: String,
    val nonce: String,
    val payload: String,
    val toAddress: String,
    val maxInclusionFeePerGas: String,
    val maxFeePerGas: String,
) : WalletSignInput

