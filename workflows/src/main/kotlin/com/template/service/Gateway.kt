package com.template.service

/**
 * @author nature
 * @date 4/9/21 上午10:57
 * @email 924943578@qq.com
 */
import com.template.flows.TransferRequestFlow
import com.template.metadata.PasswordMessage
import com.template.schema.PasswordSchemaV1
import com.template.service.eth.Event
import net.corda.core.node.AppServiceHub
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.node.services.vault.Builder.equal
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.utilities.loggerFor
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.http.HttpService
import org.web3j.quorum.Quorum
import org.web3j.quorum.tx.ClientTransactionManager
import org.web3j.tx.Contract
import org.web3j.tx.ManagedTransaction
import org.web3j.tx.TransactionManager


@CordaService
//class Gateway(val serviceHub: AppServiceHub) : SingletonSerializeAsToken() {
class Gateway(val serviceHub: ServiceHub) : SingletonSerializeAsToken() {


    private val ourIdentity = serviceHub.myInfo.legalIdentities.first()
    private val ourName = ourIdentity.name.toString()


    var eventContract : Event? = null


    private companion object {
        val log = loggerFor<Gateway>()
        val URL = "Http://localhost:8546" //GETH
        val myAddress = "0x87f34d01eb6dd0e96794ead03f86cd0a8b610c11"
        val contractAddress = "0xa677b9f33a077d642eb2e71ea13d9008bb3437cb"

    }



    init {
        initWeb3j()
        log.info("Gateway init")
    }

    fun initWeb3j() {

//        val legalIdentities = serviceHub.myInfo.legalIdentities
//        println(legalIdentities)
        val web3j = Quorum.build(HttpService(URL))
        val transactionManager: TransactionManager = ClientTransactionManager(
                web3j, myAddress, emptyList())


        eventContract = Event.load(
                contractAddress, web3j, transactionManager, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT)


        val filter = EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contractAddress)


        //Receive locked asset event -> send password to eth
        val lockedFlowable = eventContract!!.dataStoredEventFlowable(filter)


        if(ourName != null && ourName.contains("payer", true)) {
            lockedFlowable.subscribe { event ->
                run {
                    println("lockedFlowable  $event")

                    //get passwordHash from event and get password from PasswordState , then send to eth

                    var passworHash = "passwordHash001"
                    sendPassword(passworHash)
                }
            }
        }


        //Receive unlocked asset event -> trigger TransferRequest flow

        val unlockedFlowable = eventContract!!.dataStoredEventFlowable(filter)


        if(ourName != null && ourName.contains("payee", true)) {

            unlockedFlowable.subscribe { event ->
                run {
                    println("unlockedFlowable  $event")

                    //get passwordHash from event and get password from PasswordState , then send to eth
                    val passwordHash = "passwordHash001"
                    val password = "password001"
                    val passwordMessage = PasswordMessage(passwordHash, password)
                    callTransferRequestFlow(passwordMessage)
                }
            }
        }
    }

    fun sendPasswordHash(passwordHashMessage: String): String {
        //send passwordHash
        eventContract!!.storeData()
         return "";

    }

    fun sendPassword(passwordHash: String): String {
        val passwordHashRequestIdCriteria = QueryCriteria.VaultCustomQueryCriteria(
                PasswordSchemaV1.PersistentPassword::passwordHash.equal(passwordHash)
        )

//        val passwordState = serviceHub.vaultService.queryBy<PasswordState>(passwordHashRequestIdCriteria.and(passwordHashRequestIdCriteria)).states.single().state.data
//        val password = passwordState.password

        //send password
        eventContract!!.storeData()
        return "";

    }

    fun callTransferRequestFlow(passwordMessage: PasswordMessage){

        val appServiceHub = serviceHub  as AppServiceHub
        appServiceHub.startFlow(TransferRequestFlow.TransferRequest(passwordMessage))

    }


}