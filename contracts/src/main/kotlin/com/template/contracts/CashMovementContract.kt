package com.template.contracts

import com.template.metadata.CashMovementStatus
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
class CashMovementContract : Contract {
    companion object {
        @JvmStatic
        val CASH_MOVEMENT_CONTRACT_ID = "com.template.contracts.CashMovementContract"
    }

    interface Commands : CommandData {
        class CashTransferPendingCmd : TypeOnlyCommandData(), Commands

        class CashTransferRequestCmd : TypeOnlyCommandData(), Commands

    }


    override fun verify(tx: LedgerTransaction) = requireThat {
        tx.commands.forEach {

            if (it.value is Commands.CashTransferPendingCmd) {

                val cashMovementState = tx.outputsOfType<CashMovementState>().single()

                requireThat {
                    "${cashMovementState.status} must equals to to " using (cashMovementState.status == CashMovementStatus.TRANSFER_PENDING)

                }

            }else if (it.value is Commands.CashTransferRequestCmd){
                val commands = tx.commands;
                requireThat {
                    "${commands.size} must equals to to 2" using (commands.size == 2)
                    if( commands[0].value is Commands.CashTransferRequestCmd)
                        requireThat {"commands[1].value  must equals to to PasswordContract.Commands.ConsumeCmd" using (commands[1].value is PasswordHashContract.Commands.ConsumeCmd)}
                    else
                        requireThat {"commands[0].value  must equals to to PasswordContract.Commands.ConsumeCmd" using (commands[0].value is PasswordHashContract.Commands.ConsumeCmd)}

                }
            }
        }
    }
}