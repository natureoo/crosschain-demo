package com.template

import com.template.service.Gateway
import net.corda.core.identity.CordaX500Name
import net.corda.testing.core.SerializationEnvironmentRule
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import org.junit.Rule
import org.junit.Test

class GatewayTests {
    private val oracleIdentity = TestIdentity(CordaX500Name("Oracle", "New York", "US"))
    private val dummyServices = MockServices(listOf("com.template.contracts"), oracleIdentity)
    private val gateway = Gateway(dummyServices)

    @Rule
    @JvmField
    val testSerialization = SerializationEnvironmentRule()

    @Test
    fun `gateway init`() {
//        gateway.initWeb3j()
//        Thread.currentThread().join()
    }


}