package com.dimension.maskwalletcore

import androidx.test.ext.junit.runners.AndroidJUnit4
import api.Api
import api.Common

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
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
}