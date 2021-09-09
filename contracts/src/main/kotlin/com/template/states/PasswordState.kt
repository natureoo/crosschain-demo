package com.template.states

import com.template.contracts.PasswordContract
import com.template.schema.PasswordSchemaV1
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.OwnableState
import net.corda.core.identity.AbstractParty
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

        override val owner: AbstractParty,

        val passwordHash: String,

        val password: String,

        val entryDate: Instant? = Instant.now()
) :  QueryableState, OwnableState {

    override val participants: List<AbstractParty>
    get() = listOf( owner)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
         return when (schema) {
            is PasswordSchemaV1 -> PasswordSchemaV1.PersistentPassword(
            ownerName = this.owner.nameOrNull().toString(),
            entryDate = this.entryDate,
            password = this.password,
            passwordHash = this.passwordHash
        )
        else -> throw IllegalArgumentException("Unrecognised schema $schema")
    }
}

    override fun supportedSchemas(): Iterable<MappedSchema> = listOf(PasswordSchemaV1)

    override fun withNewOwner(newOwner: net.corda.core.identity.AbstractParty): net.corda.core.contracts.CommandAndState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}


