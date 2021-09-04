package com.template.contracts

import com.template.states.PasswordState
import net.corda.core.contracts.*
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
        class CreateCmd : TypeOnlyCommandData(), Commands

        class ConsumeCmd(val verifyPassword: String) : TypeOnlyCommandData(), Commands

    }


    // Our contract does not check that the Nth prime is correct. Instead, it checks that the
    // information in the command and state match.
    override fun verify(tx: LedgerTransaction) = requireThat {

        tx.commands.forEach {

                if(it.value is Commands.ConsumeCmd){

                    val consumeCommand = tx.commands.requireSingleCommand<Commands.ConsumeCmd>().value
                    val passwordState = tx.inputsOfType<PasswordState>().single()

                    requireThat {
                        "${passwordState.password} must equals to to " using (passwordState.password == consumeCommand.verifyPassword)

                    val timeWindow = tx.timeWindow
                    if (timeWindow == null || timeWindow.untilTime == null) {
                        throw IllegalArgumentException("Cake transaction must have a timestamp with an until-time.")
                    }
                    if (timeWindow.untilTime!!.isAfter(passwordState.expiry)) {
                        throw IllegalArgumentException("Expiry has passed! Expiry date & time was: " + passwordState.expiry)
                    }
                }

          }
        }

    }
}