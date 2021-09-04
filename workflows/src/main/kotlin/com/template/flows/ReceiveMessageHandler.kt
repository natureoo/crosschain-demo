package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.template.metadata.PasswordHashMessage
import com.template.service.Gateway
import net.corda.core.flows.FlowException
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.FlowSession
import net.corda.core.flows.InitiatedBy
import net.corda.core.utilities.ProgressTracker
import net.corda.core.utilities.unwrap

/**
 * @author nature
 * @date 4/9/21 上午10:44
 * @email 924943578@qq.com
 */
@InitiatedBy(SendMessageFlow::class)
class ReceiveMessageHandler(val session: FlowSession) : FlowLogic<Unit>() {
    companion object {
        object RECEIVING : ProgressTracker.Step("Receiving query request.")
        object CALCULATING : ProgressTracker.Step("Calculating Nth prime.")
        object SENDING : ProgressTracker.Step("Sending query response.")
    }

    override val progressTracker = ProgressTracker(RECEIVING, CALCULATING, SENDING)

    @Suspendable
    override fun call() {
        progressTracker.currentStep = RECEIVING
        val message = session.receive<PasswordHashMessage>().unwrap { it }

        progressTracker.currentStep = CALCULATING
        val response = try {
            // Get the nth prime from the oracle.
            serviceHub.cordaService(Gateway::class.java).sendMessage(message)
        } catch (e: Exception) {
            // Re-throw the exception as a FlowException so its propagated to the querying node.
            throw FlowException(e)
        }

        progressTracker.currentStep = SENDING
        session.send(response)
    }
}