package com.dimension.maskwalletcore

sealed interface WalletSignInput

data class EthereumSignInput(
    val amount: String,
    val chainId: Long,
    val gasLimit: String,
    val gasPrice: String,
    val nonce: String,
    val payload: ByteArray,
    val toAddress: String,
    val maxInclusionFeePerGas: String,
    val maxFeePerGas: String,
) : WalletSignInput {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EthereumSignInput

        if (amount != other.amount) return false
        if (chainId != other.chainId) return false
        if (gasLimit != other.gasLimit) return false
        if (gasPrice != other.gasPrice) return false
        if (nonce != other.nonce) return false
        if (!payload.contentEquals(other.payload)) return false
        if (toAddress != other.toAddress) return false
        if (maxInclusionFeePerGas != other.maxInclusionFeePerGas) return false
        if (maxFeePerGas != other.maxFeePerGas) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amount.hashCode()
        result = 31 * result + chainId.hashCode()
        result = 31 * result + gasLimit.hashCode()
        result = 31 * result + gasPrice.hashCode()
        result = 31 * result + nonce.hashCode()
        result = 31 * result + payload.contentHashCode()
        result = 31 * result + toAddress.hashCode()
        result = 31 * result + maxInclusionFeePerGas.hashCode()
        result = 31 * result + maxFeePerGas.hashCode()
        return result
    }
}

