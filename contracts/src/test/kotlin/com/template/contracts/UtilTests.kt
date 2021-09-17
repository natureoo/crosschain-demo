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
        val hash = Util.generateHash("0x6d7246adddbf54fb82dcb1aabacaa7a5183b898338aef11a029125ae9f618b29")
        println("hash: $hash")
    }

    @Test
    fun testGenPassword() {
        val password = Util.generatePassword()
        println("password: $password")

    }
}