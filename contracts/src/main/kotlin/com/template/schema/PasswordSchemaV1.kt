package com.template.schema

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * @author nature
 * @date 4/9/21 上午11:32
 * @email 924943578@qq.com
 */
object PasswordSchema

/**
 * An CashMovementState schema.
 */
object PasswordSchemaV1 : MappedSchema(
        schemaFamily = PasswordSchema::class.java,
        version = 1,
        mappedTypes = listOf(PersistentPassword::class.java)
) {
    @Entity
    @Table(name = "password_states")
    class PersistentPassword(

            @Column(name = "owner")
            var ownerName: String,

            @Column(name = "password")
            var password: String,

            @Column(name = "passwordHash")
            var passwordHash: String,


            @Column(name = "entry_date")
            var entryDate: Instant?
    ) : PersistentState(){
        constructor() : this("", "", "", Instant.now())
    }
}