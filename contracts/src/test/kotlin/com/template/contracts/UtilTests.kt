package com.template.contracts

import com.template.tool.Util
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
        val hash = Util.generateHash("0xc3a677bd643428a56890f9731baeb144906c2b9080dc942e01046c98be27159d")
        println("hash: $hash")
    }

    @Test
    fun testGenPassword() {
        val password = Util.generatePassword()
        println("password: $password")

    }
}