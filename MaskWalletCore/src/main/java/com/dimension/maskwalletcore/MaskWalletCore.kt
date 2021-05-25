package com.dimension.maskwalletcore

internal object MaskWalletCore {
    init {
        System.loadLibrary("maskwalletdroid")
    }

    external fun request(value: ByteArray): ByteArray

    fun call(request: Api.MWRequest): Api.MWResponse {
        return request(request.toByteArray()).let { bytes ->
            Api.MWResponse.parseFrom(bytes).let { response ->
                if (response.hasError()) {
                    throw MaskWalletCoreException(response.error.errorCode, response.error.errorMsg)
                } else {
                    response
                }
            }
        }
    }

    fun call(builder: MWRequestKt.Dsl.() -> Unit): Api.MWResponse {
        return call(mWRequest(builder))
    }
}

