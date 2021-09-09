package com.template.contracts

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.TypeOnlyCommandData
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

        class ConsumeCmd : TypeOnlyCommandData(), Commands

    }

    override fun verify(tx: LedgerTransaction) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}