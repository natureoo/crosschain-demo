package com.main

import com.template.flows.TransferPendingFlow
import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.loggerFor
import java.math.BigDecimal
import java.util.*

/**
 * Connects to a Corda node via RPC and performs RPC operations on the node.
 *
 * The RPC connection is configured using command line arguments.
 */
fun main(args: Array<String>) = FlowMain().main(args)

private class FlowMain {
    companion object {
        val logger = loggerFor<FlowMain>()
    }




    fun main(args: Array<String>) {
        // Create an RPC connection to the node.

        val host = "127.0.0.1"
        val partyAPayerPort = 10006 //developer
        val userName = "user1"
        val password = "test"

        val partyAPayerX500Name = "O=PartyAPayer,L=London,C=GB"
        val partyBPayeeX500Name = "O=PartyBPayee,L=New York,C=US"

        val partyAPayerConn = CordaNodeConnector(host, partyAPayerPort, userName, password)
        partyAPayerConn.connect()
        val partyAPayerIdentity = partyAPayerConn.proxy.wellKnownPartyFromX500Name(CordaX500Name.parse(partyAPayerX500Name))!!
        val partyBPayeeIdentity = partyAPayerConn.proxy.wellKnownPartyFromX500Name(CordaX500Name.parse(partyBPayeeX500Name))!!


        val signedTransaction = partyAPayerConn.proxy.startFlowDynamic(TransferPendingFlow.TransferPending::class.java,
                partyAPayerIdentity, partyBPayeeIdentity, BigDecimal.valueOf(100L), Currency.getInstance("SGD")
        ).returnValue.get()
        signedTransaction.coreTransaction.outputStates.forEach{println(it)}

    }
}