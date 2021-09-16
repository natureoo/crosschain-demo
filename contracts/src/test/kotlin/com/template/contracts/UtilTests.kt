package com.template.contracts

import com.template.utils.Util
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author nature
 * @date 16/9/21 上午10:18
 * @email 924943578@qq.com
 */
class UtilTests {


    @Before
    fun setup() {

    }

    @After
    fun tearDown() {
    }

    @Test
    fun testGenHash() {
        val hash = Util.generateHash("0x630a250189011b30f2a325ce1c6450c87a7e21a09e2f37e17ff15c77bdc951bd")
        println("hash: $hash")
    }

    @Test
    fun testGenPassword() {
        val password = Util.generatePassword()
        println("password: $password")

    }
}