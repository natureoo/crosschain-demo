package com.template

import com.template.flows.TransferPendingFlow
import com.template.flows.TransferRequestFlow
import com.template.metadata.PasswordMessage
import com.template.states.CashMovementState
import com.template.states.PasswordHashState
import com.template.states.PasswordState
import net.corda.core.contracts.StateAndRef
import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.getOrThrow
import net.corda.testing.core.singleIdentity
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.TestCordapp
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals

/**
 * @author nature
 * @date 4/9/21 下午6:30
 * @email 924943578@qq.com
 */
class FlowTests {

    private lateinit var network: MockNetwork
    private lateinit var payerNode: StartedMockNode
    private lateinit var payeeNode: StartedMockNode

//    private val oracleIdentity = TestIdentity(CordaX500Name("Oracle", "New York", "US"))
//    private val oracleService = MockServices(listOf("com.template.contracts"), oracleIdentity)
    private lateinit var passwordHashState: PasswordHashState
    private lateinit var passwordState: PasswordState
    private lateinit var cashMovementState: CashMovementState

    @Before
    fun setup() {
        network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = listOf(
                TestCordapp.findCordapp("com.template.contracts"),
                TestCordapp.findCordapp("com.template.flows")
        )))
        payerNode = network.createPartyNode(CordaX500Name("AlicePayer", "London", "GB"))
        payeeNode = network.createPartyNode(CordaX500Name("BobPayee", "London", "GB"))

        listOf(payerNode, payeeNode).forEach { it.registerInitiatedFlow(TransferPendingFlow.TransferPendingResponder::class.java)}
        listOf(payerNode, payeeNode).forEach { it.registerInitiatedFlow(TransferRequestFlow.TransferRequestResponder::class.java)}
//        network.runNetwork()

    }

    @After
    fun tearDown() {
        network.stopNodes()
    }

    @Test
    fun testTransferPending() {
        val flow = TransferPendingFlow.TransferPending(payerNode.info.singleIdentity(), payeeNode.info.singleIdentity(), BigDecimal.ONE, Currency.getInstance("SGD")) // instructedMVCurrency
        val future = payerNode.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.getOrThrow()
        var outputs = signedTransaction.tx.outputs
        assertEquals(3, outputs.size)
        passwordHashState = signedTransaction.tx.outputsOfType<PasswordHashState>().single()
        passwordState = signedTransaction.tx.outputsOfType<PasswordState>().single()
        cashMovementState = signedTransaction.tx.outputsOfType<CashMovementState>().single()

        Thread.currentThread().join()
    }

    @Test
    fun testTransferRequest() {
//        testTransferPending()
        val flow = TransferPendingFlow.TransferPending(payerNode.info.singleIdentity(), payeeNode.info.singleIdentity(), BigDecimal.ONE, Currency.getInstance("SGD")) // instructedMVCurrency
        val future = payerNode.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.getOrThrow()
        var outputs = signedTransaction.tx.outputs
//        passwordHashState = signedTransaction.tx.outputsOfType<PasswordHashState>().single()
        passwordState = signedTransaction.tx.outputsOfType<PasswordState>().single()

        payeeNode.transaction {
//            val ious: List<StateAndRef<CashMovementState>> = payeeNode.services.vaultService.queryBy(CashMovementState::class.java).states
//            Assert.assertEquals(1, ious.size.toLong())

            val passwordHashStates: List<StateAndRef<PasswordHashState>> = payeeNode.services.vaultService.queryBy(PasswordHashState::class.java).states
            Assert.assertEquals(1, passwordHashStates.size.toLong())

            val passwordMessage = PasswordMessage(passwordState.passwordHash, passwordState.password)
            val transferRequestFlow = TransferRequestFlow.TransferRequest(passwordMessage) // instructedMVCurrency
            val transferRequestFuture = payeeNode.startFlow(transferRequestFlow)
            network.runNetwork()
            val transferRequestSignedTransaction = transferRequestFuture.getOrThrow()
            var transferRequestOutputs = transferRequestSignedTransaction.tx.outputs
            assertEquals(2, transferRequestOutputs.size)
        }

    }
}