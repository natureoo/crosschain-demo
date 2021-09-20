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
        val hash = Util.generateHash("0x5acb5d054fad4517c1401615fb3cf59359ddf5e98caf5044b90689634f2f9251")
        println("hash: $hash")
    }

    @Test
    fun testGenPassword() {
        val password = Util.generatePassword()
        println("password: $password")

    }
}