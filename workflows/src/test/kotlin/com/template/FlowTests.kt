package com.template

import com.template.flows.TransferPendingFlow
import com.template.flows.TransferRequestFlow
import com.template.metadata.PasswordMessage
import com.template.states.PasswordState
import net.corda.core.contracts.StateAndRef
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
    private lateinit var passwordState: PasswordState

    @Before
    fun setup() {
        network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = listOf(
                TestCordapp.findCordapp("com.template.contracts"),
                TestCordapp.findCordapp("com.template.flows")
        )))
        payerNode = network.createPartyNode()
        payeeNode = network.createPartyNode()

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
        assertEquals(2, outputs.size)
        passwordState = signedTransaction.tx.outputsOfType<PasswordState>().single()

    }

    @Test
    fun testTransferRequest() {
//        testTransferPending()
        val flow = TransferPendingFlow.TransferPending(payerNode.info.singleIdentity(), payeeNode.info.singleIdentity(), BigDecimal.ONE, Currency.getInstance("SGD")) // instructedMVCurrency
        val future = payerNode.startFlow(flow)
        network.runNetwork()
        val signedTransaction = future.getOrThrow()
        var outputs = signedTransaction.tx.outputs
        passwordState = signedTransaction.tx.outputsOfType<PasswordState>().single()

        payeeNode.transaction {
//            val ious: List<StateAndRef<CashMovementState>> = payeeNode.services.vaultService.queryBy(CashMovementState::class.java).states
//            Assert.assertEquals(1, ious.size.toLong())

            val passwordStates: List<StateAndRef<PasswordState>> = payeeNode.services.vaultService.queryBy(PasswordState::class.java).states
            Assert.assertEquals(1, passwordStates.size.toLong())

            val passwordMessage = PasswordMessage(passwordState.requestId, passwordState.password)
            val transferRequestFlow = TransferRequestFlow.TransferRequest(passwordMessage) // instructedMVCurrency
            val transferRequestFuture = payeeNode.startFlow(transferRequestFlow)
            network.runNetwork()
            val transferRequestSignedTransaction = transferRequestFuture.getOrThrow()
            var transferRequestOutputs = transferRequestSignedTransaction.tx.outputs
            assertEquals(2, transferRequestOutputs.size)
        }

    }
}