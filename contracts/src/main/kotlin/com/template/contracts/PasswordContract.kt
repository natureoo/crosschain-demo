package com.template.contracts

import com.template.states.CashMovementState
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.contracts.requireThat
import net.corda.core.transactions.LedgerTransaction

/**
 * @author nature
 * @date 3/9/21 下午12:15
 * @email 924943578@qq.com
 */
class PasswordContract : Contract {
    companion object {
        @JvmStatic
        val PASSWORD_CONTRACT_ID = "com.template.contracts.PasswordContract"
    }

    interface Commands : CommandData {
        class Create : TypeOnlyCommandData(), Commands

        class CashTransferRequestCmd : TypeOnlyCommandData(), Commands
        class CashTransferCompleteCmd : TypeOnlyCommandData(), Commands

    }


    // Our contract does not check that the Nth prime is correct. Instead, it checks that the
    // information in the command and state match.
    override fun verify(tx: LedgerTransaction) = requireThat {
        "There are no inputs" using (tx.inputs.isEmpty())
        val output = tx.outputsOfType<CashMovementState>().single()
//        val command = tx.commands.requireSingleCommand<Create>().value
//        "The prime in the output does not match the prime in the command." using
//                (command.n == output.n && command.nthPrime == output.nthPrime)
    }
}