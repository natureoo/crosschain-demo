package com.template.states

import com.template.contracts.CashMovementContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import java.math.BigDecimal
import java.time.Instant
import java.util.*

/**
 * @author nature
 * @date 3/9/21 上午11:45
 * @email 924943578@qq.com
 */
@BelongsToContract(CashMovementContract::class)
data class CashMovementState (

    val payer: Party,
    val payee: Party,

    val instructedMVUnit: BigDecimal,
    val instructedMVCurrency: Currency,

    val requestId: String,
    val entryDate: Instant? = Instant.now(),
    val status: CashMovementStatus,
    override val linearId: UniqueIdentifier = UniqueIdentifier()
    ) : LinearState, QueryableState {

        override val participants: List<AbstractParty>
        get() = listOf( payer, payee)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun supportedSchemas(): Iterable<MappedSchema> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
