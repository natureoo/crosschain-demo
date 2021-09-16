package com.template.contracts

import com.template.states.PasswordHashState
import com.template.utils.Util
import net.corda.core.contracts.*
import net.corda.core.transactions.LedgerTransaction

/**
 * @author nature
 * @date 3/9/21 下午12:15
 * @email 924943578@qq.com
 */
class PasswordHashContract : Contract {
    companion object {
        @JvmStatic
        val PASSWORD_HASH_CONTRACT_ID = "com.template.contracts.PasswordHashContract"
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
                    val passwordHashState = tx.inputsOfType<PasswordHashState>().single()
                    val verifyPasswordHash = Util.generateHash(consumeCommand.verifyPassword)

                    requireThat {
                        "${passwordHashState.passwordHash} must equals to to " using (passwordHashState.passwordHash == verifyPasswordHash)

                    val timeWindow = tx.timeWindow
                    if (timeWindow == null || timeWindow.untilTime == null) {
                        throw IllegalArgumentException("Cake transaction must have a timestamp with an until-time.")
                    }
                    if (timeWindow.untilTime!!.isAfter(passwordHashState.expiry)) {
                        throw IllegalArgumentException("Expiry has passed! Expiry date & time was: " + passwordHashState.expiry)
                    }
                }

          }
        }

    }
}