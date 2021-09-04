package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.template.contracts.CashMovementContract
import com.template.contracts.PasswordContract
import com.template.states.CashMovementState
import com.template.states.CashMovementStatus
import com.template.states.PasswordState
import com.template.utils.setStep
import net.corda.core.contracts.Command
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.Party
import net.corda.core.internal.randomOrNull
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import java.math.BigDecimal
import java.math.BigInteger
import java.security.MessageDigest
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author nature
 * @date 3/9/21 下午2:43
 * @email 924943578@qq.com
 */
class TransferPendingFlow {

    @InitiatingFlow
    @StartableByRPC
    class TransferRequest(
            val payer: Party,
            val payee: Party,
            val instructedMVUnit: BigDecimal,
            val instructedMVCurrency: Currency
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
            setStep(GENERATING_TRANSACTION)
            val requestId = UUID.randomUUID().toString()
            val cashMovementState = CashMovementState(
                    payer = payer,
                    payee = payee,

                    instructedMVUnit = instructedMVUnit,
                    instructedMVCurrency = instructedMVCurrency,
                    status = CashMovementStatus.TRANSFER_PENDING,
                    requestId = requestId,
                    entryDate = Instant.now()
            )

            val pwd = ThreadLocalRandom.current().toString()
            val passwordState = PasswordState(
                    payer = payer,
                    password = pwd,
                    passwordHash = getHash(pwd),
                    requestId = UUID.randomUUID().toString(),
                    entryDate = Instant.now()
            )


            val cashTransferPendingCmd = Command(CashMovementContract.Commands.CashTransferPendingCmd(),
                    cashMovementState.payer.owningKey)
            val passwordCreateCmd = Command(PasswordContract.Commands.Create(),
                    passwordState.payer.owningKey)
            val txBuilder = TransactionBuilder(notary).apply {
                addOutputState(cashMovementState, CashMovementContract.CASH_MOVEMENT_CONTRACT_ID, notary, 1)
                addOutputState(passwordState, PasswordContract.PASSWORD_CONTRACT_ID)
                addCommand(cashTransferPendingCmd)
                addCommand(passwordCreateCmd)
                setTimeWindow(Instant.now(), Duration.ofHours(2));
            }

            // Stage 2 - Verifying Transaction
            setStep(VERIFYING_TRANSACTION)
            // Verify that the transaction is valid
            txBuilder.verify(serviceHub)

            // Stage 3 - Signing Transaction
            setStep(SIGNING_TRANSACTION)
            // Sign the transaction
            val signedTx = serviceHub.signInitialTransaction(txBuilder)

            // Stage 4 - Finalizing Transaction
            val receivers = listOf(payer, payee).filter {!it.equals(ourIdentity) }.map { party -> initiateFlow(party) }
            return subFlow(FinalityFlow(signedTx, receivers, FINALISING_TRANSACTION.childProgressTracker()))
        }

        fun getHash(input:String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }
    }
}