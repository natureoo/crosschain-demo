package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.template.metadata.PasswordHashMessage
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.identity.Party
import net.corda.core.utilities.unwrap

/**
 * @author nature
 * @date 4/9/21 上午9:57
 * @email 924943578@qq.com
 */
@InitiatingFlow
class SendMessageFlow(val targetParty: Party, val passwordHashMessage: PasswordHashMessage) : FlowLogic<PasswordHashMessage>() {
    @Suspendable
    override fun call() = initiateFlow(targetParty).sendAndReceive<PasswordHashMessage>(passwordHashMessage).unwrap { it }
}