package com.dimension.maskwalletcore

data class JsonWebKey(
    val kty: String = "",
    val kid: String? = null,
    val use: String? = null,
    val key_ops: List<String>? = null,
    val alg: String? = null,
    val ext: Boolean? = null,
    val crv: String? = null,
    val x: String? = null,
    val y: String? = null,
    val d: String? = null,
    val n: String? = null,
    val e: String? = null,
    val p: String? = null,
    val q: String? = null,
    val dp: String? = null,
    val dq: String? = null,
    val qi: String? = null,
    val oth: List<RsaOtherPrimesInfo>? = null,
    val k: String? = null,
) {
    data class RsaOtherPrimesInfo(val r: String, val d: String, val t: String)
    companion object {
        internal fun fromJWKResp(resp: Persona.JWKResp): JsonWebKey {
            return JsonWebKey(
                kty = resp.kty,
                key_ops = resp.keyOpsList,
                ext = resp.ext,
                crv = resp.crv,
                x = resp.x,
                y = resp.y,
                d = if (resp.hasD()) resp.d else null,
            )
        }

        internal fun fromAesJWKResp(resp: Persona.AesJWKResp): JsonWebKey {
            return JsonWebKey(
                kty = resp.kty,
                key_ops = resp.keyOpsList,
                ext = resp.ext,
                k = resp.k,
                alg = resp.alg,
            )
        }
    }
}