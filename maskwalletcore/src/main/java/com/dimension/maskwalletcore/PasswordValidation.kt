package com.dimension.maskwalletcore

data class PasswordValidation(
    val storedKey: ByteArray,
    val password: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PasswordValidation

        if (!storedKey.contentEquals(other.storedKey)) return false
        if (password != other.password) return false

        return true
    }

    override fun hashCode(): Int {
        var result = storedKey.contentHashCode()
        result = 31 * result + password.hashCode()
        return result
    }
}