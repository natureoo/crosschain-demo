package com.template

import com.template.service.Gateway
import net.corda.core.identity.CordaX500Name
import net.corda.testing.core.SerializationEnvironmentRule
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import org.junit.Rule
import org.junit.Test

class GatewayTests {
    private val oracleIdentity = TestIdentity(CordaX500Name("OraclePayer", "New York", "US"))
    private val dummyServices = MockServices(listOf("com.template.contracts"), oracleIdentity)
    private val gateway = Gateway(dummyServices)

    @Rule
    @JvmField
    val testSerialization = SerializationEnvironmentRule()

    @Test
    fun `gateway init`() {
//        gateway.initWeb3j()
        gateway.sendPasswordHash("0xf972aa9efc9f921e7b44d732471a1cada96523c7e9799a3c80ee249f7f5106d1")
        Thread.currentThread().join()
    }


}