package com.dimension.maskwalletcore

data class SignResult(
    val encoded: ByteArray,
    val v: Int,
    val r: ByteArray,
    val s: ByteArray,
    val data: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SignResult

        if (!encoded.contentEquals(other.encoded)) return false
        if (v != other.v) return false
        if (!r.contentEquals(other.r)) return false
        if (!s.contentEquals(other.s)) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = encoded.contentHashCode()
        result = 31 * result + v
        result = 31 * result + r.contentHashCode()
        result = 31 * result + s.contentHashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}