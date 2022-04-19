package com.dimension.maskwalletcore

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class TestMaskWalletCore {
    @Test
    fun testVersion() {
        val input = Api.MWRequest.newBuilder()
            .setParamGetVersion(Common.GetVersionParam.newBuilder().build())
            .build()
            .toByteArray()
        val output = MaskWalletCore.request(input).let {
            Api.MWResponse.parseFrom(it)
        }.respGetVersion
        assertNotNull(output)
    }

    @Test
    fun testWalletKetCreation() {
        val password = "123456"
        val (key, mnemonic) = WalletKey.create(password = password)
        assert(mnemonic.isNotEmpty())
        assert(mnemonic.isNotBlank())
        assert(key.id.isNotEmpty())
        assert(key.hash.isNotEmpty())
    }

    @Test
    fun testWalletRestoreByMnemonic() {
        val password = "123456"
        val (key, mnemonic) = WalletKey.create(password = password)
        val restoredKey = WalletKey.fromMnemonic(
            mnemonic = mnemonic,
            password = password,
        )
        assertEquals(key.hash, restoredKey.hash)
    }

    @Test
    fun testPersonaCreate() {
        val password = "123456"
        val mnemonic = WalletKey.generateMnemonic()
        val path = "m/44'/60'/0'/0/0"
        val curveType = CurveType.SECP256K1
        val key = PersonaKey.create(
            mnemonic = mnemonic,
            password = password,
            path = path,
            curveType = curveType,
            option = EncryptionOption(EncryptionOption.Version.V37),
        )
        assert(key.identifier.isNotEmpty())
    }
}