package com.dimension.maskwalletcore

import com.dimension.maskwalletcore.Persona.PersonaGenerationResp

class PersonaKey private constructor(
    private val resp: PersonaGenerationResp,
) {
    companion object {
        fun create(
            mnemonic: String,
            password: String,
            path: String,
            curveType: CurveType,
            option: EncryptionOption,
        ): PersonaKey {
            return MaskWalletCore.call {
                paramGeneratePersona = personaGenerationParam {
                    this.mnemonic = mnemonic
                    this.password = password
                    this.path = path
                    this.curve = curveType.toCurve()
                    this.option = option.toOption()
                }
            }.respGeneratePersona.let { PersonaKey(it) }
        }
    }

    val identifier: String
        get() = resp.identifier

    val privateKey: JsonWebKey
        get() = JsonWebKey.fromJWKResp(resp.privateKey)

    val publicKey: JsonWebKey
        get() = JsonWebKey.fromJWKResp(resp.publicKey)

    val localKey: JsonWebKey?
        get() = if (resp.hasLocalKey()) JsonWebKey.fromAesJWKResp(resp.localKey) else null
}