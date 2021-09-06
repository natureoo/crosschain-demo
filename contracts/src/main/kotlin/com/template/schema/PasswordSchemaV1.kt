package com.template.schema

import com.template.metadata.PasswordStatus
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

            @Column(name = "payer")
            var payerName: String,

            @Column(name = "payee")
            var payeeName: String,

            @Column(name = "password")
            var password: String,

            @Column(name = "passwordHash")
            var passwordHash: String,

            @Column(name = "requestId")
            var requestId: String,

            @Column(name = "expiry")
            var expiry: Instant,

            @Column(name = "entry_date")
            var entryDate: Instant?,
            @Column(name = "status")
            var status: PasswordStatus
    ) : PersistentState(){
        constructor() : this("", "", "", "", "", Instant.now(), Instant.now(), PasswordStatus.ACTIVE)
    }
}