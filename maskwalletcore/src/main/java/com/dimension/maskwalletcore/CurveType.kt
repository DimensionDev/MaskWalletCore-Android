package com.dimension.maskwalletcore

enum class CurveType {
    SECP256K1,
    ED25519;
    internal fun toCurve(): Persona.PersonaGenerationParam.Curve {
        return when (this) {
            SECP256K1 -> Persona.PersonaGenerationParam.Curve.Secp256k1
            ED25519 -> Persona.PersonaGenerationParam.Curve.Ed25519
        }
    }
}