package com.template.schema

import com.template.metadata.CashMovementStatus
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * @author nature
 * @date 4/9/21 上午11:25
 * @email 924943578@qq.com
 */
/**
 * The family of schemas for CashMovementState.
 */
object CashMovementSchema

/**
 * An CashMovementState schema.
 */
object CashMovementSchemaV1 : MappedSchema(
        schemaFamily = CashMovementSchema::class.java,
        version = 1,
        mappedTypes = listOf(PersistentCashMovement::class.java)
) {
    @Entity
    @Table(name = "cash_movement_states")
    class PersistentCashMovement(

            @Column(name = "payer")
            var payerName: String,
            @Column(name = "payee")
            var payeeName: String,

            @Column(name = "instructed_mv_unit")
            var instructedMVUnit: BigDecimal,

            @Column(name = "instructed_mv_currency")
            var instructedMVCurrency: Currency,

            @Column(name = "requestId")
            var requestId: String,

            @Column(name = "entry_date")
            var entryDate: Instant?,
            @Column(name = "status")
            var status: CashMovementStatus?,
            @Column(name = "linear_id")
            var linearId: UUID
    ) : PersistentState(){
        constructor() : this("", "", BigDecimal.ZERO, Currency.getInstance("SGD"), "", Instant.now(), CashMovementStatus.TRANSFER_PENDING, UUID.randomUUID())

    }
}