package com.dimension.maskwalletcore

data class EncryptionOption(
    val version: Version,
) {
    enum class Version {
        V37,
        V38;
        internal fun toVersion(): Base.EncryptOption.Version {
            return when (this) {
                V37 -> Base.EncryptOption.Version.V37
                V38 -> Base.EncryptOption.Version.V38
            }
        }
    }

    internal fun toOption(): Base.EncryptOption {
        return encryptOption {
            version = this@EncryptionOption.version.toVersion()
        }
    }
}