package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.template.contracts.CashMovementContract
import com.template.contracts.PasswordContract
import com.template.metadata.PasswordMessage
import com.template.schema.CashMovementSchemaV1
import com.template.schema.PasswordSchemaV1
import com.template.states.CashMovementState
import com.template.states.CashMovementStatus
import com.template.states.PasswordState
import com.template.utils.setStep
import net.corda.core.contracts.Command
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.internal.randomOrNull
import net.corda.core.node.services.queryBy
import net.corda.core.node.services.vault.Builder.equal
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

/**
 * @author nature
 * @date 3/9/21 下午2:43
 * @email 924943578@qq.com
 */
class TransferRequestFlow {

    @InitiatingFlow
    @StartableByRPC
    class TransferRequest(
            val passwordMessage: PasswordMessage
    ) : FlowLogic<SignedTransaction>() {
        /**
         * The progress tracker checkpoints each stage of the flow and outputs the specified messages when each
         * checkpoint is reached in the code.
         */
        companion object {
            object GENERATING_TRANSACTION : ProgressTracker.Step("Generating transaction based on a new Cash Transfer Request")
            object VERIFYING_TRANSACTION : ProgressTracker.Step("Verifying contract constraints")
            object SIGNING_TRANSACTION : ProgressTracker.Step("Signing transaction with our private key")
            object FINALISING_TRANSACTION : ProgressTracker.Step("Obtaining notary signature and recording transaction") {
                override fun childProgressTracker(): ProgressTracker = FinalityFlow.tracker()
            }

            fun tracker() = ProgressTracker(
                    GENERATING_TRANSACTION,
                    VERIFYING_TRANSACTION,
                    SIGNING_TRANSACTION,
                    FINALISING_TRANSACTION
            )
        }

        override val progressTracker = tracker()

        /**
         * The flow logic is encapsulated within a call() method
         */
        @Suspendable
        override fun call(): SignedTransaction {
            // obtain a reference from a notary we wish to use
            val notary = serviceHub.networkMapCache.notaryIdentities.randomOrNull()
                    ?: throw IllegalStateException("Empty notaryIdentities")
            // Stage 1 - Generating Transaction


            val passwordRequestIdCriteria = QueryCriteria.VaultCustomQueryCriteria(
                    PasswordSchemaV1.PersistentPassword::requestId.equal(passwordMessage.requestId)
            )

            val passwordState = serviceHub.vaultService.queryBy<PasswordState>(passwordRequestIdCriteria).states.single()

            val cashMovementRequestIdCriteria = QueryCriteria.VaultCustomQueryCriteria(
                    CashMovementSchemaV1.PersistentCashMovement::requestId.equal(passwordMessage.requestId)
            )

            val cashMovementPendingStateAndRef = serviceHub.vaultService.queryBy<CashMovementState>(cashMovementRequestIdCriteria).states.single()


            val cashMovementRequestState = cashMovementPendingStateAndRef.state.data.copy(
                    status = CashMovementStatus.TRANSFER_REQUEST
            )

            val cashTransferRequestCmd = Command(CashMovementContract.Commands.CashTransferRequestCmd(),
                    cashMovementRequestState.payer.owningKey)
            val passwordConsumedCmd = Command(PasswordContract.Commands.ConsumeCmd(passwordMessage.password),
                    cashMovementRequestState.payer.owningKey)

            val txBuilder = TransactionBuilder(notary).apply {
                addInputState(cashMovementPendingStateAndRef)
                addInputState(passwordState)

                addOutputState(cashMovementRequestState, CashMovementContract.CASH_MOVEMENT_CONTRACT_ID, notary, 1) // Encumbrance is at index 1
                addCommand(cashTransferRequestCmd)
                addCommand(passwordConsumedCmd)
            }

            // Stage 2 - Verifying Transaction
            setStep(TransferPendingFlow.TransferRequest.Companion.VERIFYING_TRANSACTION)
            // Verify that the transaction is valid
            txBuilder.verify(serviceHub)

            // Stage 3 - Signing Transaction
            setStep(TransferPendingFlow.TransferRequest.Companion.SIGNING_TRANSACTION)
            // Sign the transaction
            val signedTx = serviceHub.signInitialTransaction(txBuilder)

            // Stage 4 - Finalizing Transaction
            val receivers = listOf(cashMovementRequestState.payer, cashMovementRequestState.payee).filter {!it.equals(ourIdentity) }.map { party -> initiateFlow(party) }
            return subFlow(FinalityFlow(signedTx, receivers, TransferPendingFlow.TransferRequest.Companion.FINALISING_TRANSACTION.childProgressTracker()))

        }


    }
}