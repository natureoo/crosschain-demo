package com.template.states

import com.template.metadata.PasswordStatus
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import java.time.Instant

/**
 * @author nature
 * @date 3/9/21 上午11:45
 * @email 924943578@qq.com
 */
data class PasswordState (

        val payer: Party,

        val password: String,

        val passwordHash: String,

        val requestId: String,

        val expiry: Instant,

        val status: PasswordStatus = PasswordStatus.ACTIVE,

        val entryDate: Instant? = Instant.now()
    ) :  QueryableState {

    override val participants: List<AbstractParty>
    get() = listOf( payer)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun supportedSchemas(): Iterable<MappedSchema> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
