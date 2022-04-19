package com.dimension.maskwalletcore

import com.google.protobuf.ByteString

class WalletKey private constructor(
    private var storedKey: Base.StoredKeyInfo
) {
    companion object {
        fun load(
            vararg keys: ByteArray
        ): List<WalletKey> {
            return MaskWalletCore.call {
                paramLoadStoredKey = loadStoredKeyParam {
                    this.data.addAll(keys.map { ByteString.copyFrom(it) })
                }
            }.respLoadStoredKey.storedKeysList.map { WalletKey(it) }
        }

        fun create(
            password: String,
        ): CreateKeyResult {
            return MaskWalletCore.call {
                paramCreateStoredKey = createStoredKeyParam {
                    this.password = password
                }
            }.respCreateStoredKey.let {
                CreateKeyResult(
                    key = WalletKey(it.storedKey),
                    mnemonic = it.mnemonic,
                )
            }
        }

        fun generateMnemonic(): String {
            return MaskWalletCore.call {
                paramGenerateMnemonic = generateMnemonicParam { }
            }.respGenerateMnemonic.mnemonic
        }

        fun fromMnemonic(
            mnemonic: String,
            password: String,
        ): WalletKey {
            val response = MaskWalletCore.call {
                paramImportMnemonic = importMnemonicStoredKeyParam {
                    this.mnemonic = mnemonic
                    this.password = password
                }
            }.respImportMnemonic.storedKey
            return WalletKey(response)
        }

        fun fromJson(
            json: String,
            name: String,
            coinType: CoinType,
            password: String,
            keyStoreJsonPassword: String,
        ): WalletKey {
            val response = MaskWalletCore.call {
                paramImportJson = importJSONStoredKeyParam {
                    this.json = json
                    this.password = password
                    this.keyStoreJsonPassword = keyStoreJsonPassword
                    this.name = name
                    this.coin = coinType.toCoin()
                }
            }.respImportJson.storedKey
            return WalletKey(response)
        }

        fun fromPrivateKey(
            privateKey: String,
            name: String,
            coinType: CoinType,
            password: String,
        ): WalletKey {
            val response = MaskWalletCore.call {
                paramImportPrivateKey = importPrivateStoredKeyParam {
                    this.privateKey = privateKey
                    this.password = password
                    this.name = name
                    this.coin = coinType.toCoin()
                }
            }.respImportPrivateKey.storedKey
            return WalletKey(response)
        }

        fun validate(
            privateKey: String? = null,
            mnemonic: String? = null,
            keyStoreJSON: String? = null,
            address: AddressValidation? = null,
            password: PasswordValidation? = null,
        ): Boolean {
            require(
                privateKey != null || mnemonic != null || keyStoreJSON != null || address != null || password != null
            )
            return MaskWalletCore.call {
                paramValidation = validateParam {
                    if (privateKey != null) {
                        this.privateKey = privateKey
                    }
                    if (mnemonic != null) {
                        this.mnemonic = mnemonic
                    }
                    if (keyStoreJSON != null) {
                        this.keyStoreJSON = keyStoreJSON
                    }
                    if (address != null) {
                        this.addressValidationParam = addressValidationParam {
                            this.coin = address.coinType.toCoin()
                            this.address = address.address
                        }
                    }
                    if (password != null) {
                        this.storedKeyPassword = passwordValidationParam {
                            this.password = password.password
                            this.storedKeyData = ByteString.copyFrom(password.storedKey)
                        }
                    }
                }
            }.respValidate.valid
        }

        fun supportedImportType(
            coinType: CoinType,
        ): List<ImportExportType> {
            return MaskWalletCore.call {
                paramGetStoredKeyImportType = getKeyStoreSupportImportTypeParam {
                    this.coin = coinType.toCoin()
                }
            }.respGetStoredKeyImportType.typeList.map {
                it.toImportType()
            }
        }

        fun supportedExportType(
            coinType: CoinType,
        ): List<ImportExportType> {
            return MaskWalletCore.call {
                paramGetStoredKeyExportType = getKeyStoreSupportExportTypeParam {
                    this.coin = coinType.toCoin()
                }
            }.respGetStoredKeyExportType.typeList.map {
                it.toImportType()
            }
        }
    }

    val id: String
        get() = storedKey.id
    val hash: String
        get() = storedKey.hash
    val type: WalletKeyType
        get() = storedKey.type.toKeyType()
    val data: ByteArray
        get() = storedKey.data.toByteArray()

    fun addNewAccountAtPath(
        coinType: CoinType,
        derivationPath: String,
        name: String,
        password: String,
    ): WalletAccount {
        return MaskWalletCore.call {
            paramCreateAccountOfCoinAtPath = createStoredKeyNewAccountAtPathParam {
                this.name = name
                this.password = password
                this.coin = coinType.toCoin()
                this.derivationPath = derivationPath
                this.storedKeyData = storedKey.data
            }
        }.respCreateAccountOfCoinAtPath.let {
            storedKey = it.storedKey
            WalletAccount(it.account)
        }
    }

    fun exportMnemonic(
        password: String,
    ): String {
        return MaskWalletCore.call {
            paramExportMnemonic = exportKeyStoreMnemonicParam {
                this.storedKeyData = storedKey.data
                this.password = password
            }
        }.respExportMnemonic.mnemonic
    }

    fun exportPrivateKey(
        coinType: CoinType,
        password: String,
    ): String {
        return MaskWalletCore.call {
            paramExportPrivateKey = exportKeyStorePrivateKeyParam {
                this.coin = coinType.toCoin()
                this.password = password
                this.storedKeyData = storedKey.data
            }
        }.respExportPrivateKey.privateKey
    }

    fun exportPrivateKeyPath(
        coinType: CoinType,
        derivationPath: String,
        password: String,
    ): String {
        return MaskWalletCore.call {
            paramExportPrivateKeyOfPath = exportKeyStorePrivateKeyOfPathParam {
                this.coin = coinType.toCoin()
                this.derivationPath = derivationPath
                this.password = password
                this.storedKeyData = storedKey.data
            }
        }.respExportPrivateKey.privateKey
    }

    fun exportKeyStoreJsonOfAddress(
        coinType: CoinType,
        address: String,
        password: String,
        newPassword: String,
    ): String {
        return MaskWalletCore.call {
            paramExportKeyStoreJsonOfAddress = exportKeyStoreJSONOfAddressParam {
                this.address = address
                this.coin = coinType.toCoin()
                this.password = password
                this.newPassword = newPassword
                this.storedKeyData = storedKey.data
            }
        }.respExportKeyStoreJson.json
    }

    fun exportKeyStoreJsonOfPath(
        coinType: CoinType,
        derivationPath: String,
        password: String,
        newPassword: String,
    ): String {
        return MaskWalletCore.call {
            paramExportKeyStoreJsonOfPath = exportKeyStoreJSONOfPathParam {
                this.coin = coinType.toCoin()
                this.derivationPath = derivationPath
                this.password = password
                this.newPassword = newPassword
                this.storedKeyData = storedKey.data
            }
        }.respExportKeyStoreJson.json
    }

    fun updatePassword(
        oldPassword: String,
        newPassword: String,
    ) {
        MaskWalletCore.call {
            paramUpdateKeyStorePassword = updateStoredKeyPasswordParam {
                this.newPassword = newPassword
                this.oldPassword = oldPassword
                this.storedKeyData = storedKey.data
            }
        }.respUpdateKeyStorePassword.storedKey.let {
            storedKey = it
        }
    }

    fun sign(
        derivationPath: String,
        coinType: CoinType,
        password: String,
        input: WalletSignInput,
    ): SignResult {
        return MaskWalletCore.call {
            paramSignTransaction = signTransactionParam {
                this.coin = coinType.toCoin()
                this.password = password
                this.derivationPath = derivationPath
                when (input) {
                    is EthereumSignInput -> this.signInput = signInput {
                        this.amount = input.amount
                        this.chainId = input.chainId
                        this.gasLimit = input.gasLimit
                        this.gasPrice = input.gasPrice
                        this.nonce = input.nonce
                        this.payload = ByteString.copyFrom(input.payload)
                        this.toAddress = input.toAddress
                        this.maxFeePerGas = input.maxFeePerGas
                        this.maxInclusionFeePerGas = input.maxInclusionFeePerGas
                    }
                }
                this.storedKeyData = storedKey.data
            }
        }.respSignTransaction.signOutput.let {
            SignResult(
                it.encoded.toByteArray(),
                it.v,
                it.r.toByteArray(),
                it.s.toByteArray(),
                it.data.toByteArray()
            )
        }
    }
}
