package com.template.states

import com.template.contracts.PasswordContract
import com.template.metadata.PasswordStatus
import com.template.schema.PasswordSchemaV1
import net.corda.core.contracts.BelongsToContract
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
@BelongsToContract(PasswordContract::class)
data class PasswordState (

        val payer: Party,
        val payee: Party,


        val password: String,

        val passwordHash: String,

        val requestId: String,

        val expiry: Instant,

        val status: PasswordStatus = PasswordStatus.ACTIVE,

        val entryDate: Instant? = Instant.now()
    ) :  QueryableState {

    override val participants: List<AbstractParty>
    get() = listOf( payer, payee)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
         return when (schema) {
            is PasswordSchemaV1 -> PasswordSchemaV1.PersistentPassword(
            payerName = this.payer.nameOrNull().toString(),
            payeeName = this.payee.name.toString(),
            status = this.status,
            requestId = this.requestId,
            entryDate = this.entryDate,
            expiry = this.expiry,
            password = this.password,
            passwordHash = this.passwordHash
        )
        else -> throw IllegalArgumentException("Unrecognised schema $schema")
    }
}

    override fun supportedSchemas(): Iterable<MappedSchema> = listOf(PasswordSchemaV1)



}

