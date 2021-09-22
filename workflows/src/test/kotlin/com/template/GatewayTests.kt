package com.template

import net.corda.core.identity.CordaX500Name
import net.corda.testing.core.SerializationEnvironmentRule
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import org.junit.Rule
import org.junit.Test

class GatewayTests {
    private val oracleIdentity = TestIdentity(CordaX500Name("OraclePayer", "New York", "US"))
    private val dummyServices = MockServices(listOf("com.template.contracts"), oracleIdentity)
//    private val gateway = Gateway(dummyServices)

    @Rule
    @JvmField
    val testSerialization = SerializationEnvironmentRule()

    @Test
    fun `gateway init`() {
//        gateway.initWeb3j()
//        gateway.sendPasswordHash("0xabd0d93e893d5a7299c93c41c8db8a74a51f9bd68449880ed2eb2975d5be13a3")
        Thread.currentThread().join()
    }


}