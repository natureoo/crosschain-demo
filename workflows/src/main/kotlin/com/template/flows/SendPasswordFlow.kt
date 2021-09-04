package com.template.flows

import co.paralleluniverse.fibers.Suspendable
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
class SendPasswordFlow(val targetParty: Party, val password: String) : FlowLogic<String>() {
    @Suspendable
    override fun call() = initiateFlow(targetParty).sendAndReceive<String>(password).unwrap { it }
}