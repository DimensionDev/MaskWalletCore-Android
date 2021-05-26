package com.dimension.maskwalletcore

enum class ImportExportType {
    PrivateKey,
    Mnemonic,
    KeyStoreJSON,
    UNRECOGNIZED,
}

internal fun Base.StoredKeyImportType.toImportType(): ImportExportType {
    return when (this) {
        Base.StoredKeyImportType.PrivateKeyImportType -> ImportExportType.PrivateKey
        Base.StoredKeyImportType.MnemonicImportType -> ImportExportType.Mnemonic
        Base.StoredKeyImportType.KeyStoreJSONImportType -> ImportExportType.KeyStoreJSON
        Base.StoredKeyImportType.UNRECOGNIZED -> ImportExportType.UNRECOGNIZED
    }
}
