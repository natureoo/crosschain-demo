package com.template.service

/**
 * @author nature
 * @date 4/9/21 上午10:57
 * @email 924943578@qq.com
 */
import com.template.flows.TransferRequestFlow
import com.template.metadata.ETHAccount
import com.template.metadata.PasswordMessage
import com.template.schema.PasswordSchemaV1
import com.template.service.eth.HashedTimelockERC20
import com.template.states.PasswordState
import com.template.utils.Util
import net.corda.core.node.AppServiceHub
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.node.services.queryBy
import net.corda.core.node.services.vault.Builder.equal
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.utilities.loggerFor
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.http.HttpService
import org.web3j.quorum.Quorum
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger


@CordaService
//class Gateway(val serviceHub: AppServiceHub) : SingletonSerializeAsToken() {
class Gateway(val serviceHub: ServiceHub) : SingletonSerializeAsToken() {


    private val ourIdentity = serviceHub.myInfo.legalIdentities.first()
    private val ourName = ourIdentity.name.toString()

    private val tokenContract = "0x69b726d0cec2c6d9026473b3e7ce91e71b58e70e"
    //account A
    private val payerETHAccount = ETHAccount("0x3761940D8aDd75AC0A2f37670a62A71c905475b5", "2d5829a087b9c70d35965ba6357d2269692b6d070df1b941571d9fd1b8b841c4")

    //account B
    private val payeeETHAccount = ETHAccount("0xD8227ed611b855D74D1D0F60Da995956306c5577", "f279649ae6910401d77acb4e4e204244f93410429a0af02967fc110f4471f6ae")

//    var eventContract : Event? = null

    var payerHtlcContract: HashedTimelockERC20? = null

    var payeeHtlcContract: HashedTimelockERC20? = null


    private companion object {
        val log = loggerFor<Gateway>()
        val URL = "https://rinkeby.infura.io/v3/b8a7ec5d22c14bd6b2ffca2352395ed4" //GETH
//        val myAddress = "0x87f34d01eb6dd0e96794ead03f86cd0a8b610c11"
        val contractAddress = "0x0Df27921cc368434C89096A6847c6cC01D17F180"

    }



    init {
        initWeb3j()
        log.info("Gateway init")
    }

    fun initWeb3j() {

//        val legalIdentities = serviceHub.myInfo.legalIdentities
//        println(legalIdentities)
        val web3j = Quorum.build(HttpService(URL))
//        val transactionManager: TransactionManager = ClientTransactionManager(
//                web3j, myAddress, emptyList())


        payerHtlcContract = HashedTimelockERC20.load(
                contractAddress, web3j, Credentials.create(payerETHAccount.privateKey), DefaultGasProvider())

        payeeHtlcContract = HashedTimelockERC20.load(
                contractAddress, web3j, Credentials.create(payeeETHAccount.privateKey), DefaultGasProvider())


        val filter = EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contractAddress)


        //Receive locked asset event -> send password to eth
        val lockedFlowable = payerHtlcContract!!.depositFundsEventFlowable(filter)


        if(ourName != null && ourName.contains("payer", true)) {
            lockedFlowable.subscribe { event ->
                run {
                    println("lockedFlowable  $event")

                    //get passwordHash from event and get password from PasswordState , then send to eth

                    var passworHash = Util.toHexString(event.hashlock)
                    sendPassword(passworHash)
                }
            }
        }


        //Receive unlocked asset event -> trigger TransferRequest flow

        val unlockedFlowable = payerHtlcContract!!.withdrawFundsEventFlowable(filter)


        if(ourName != null && ourName.contains("payee", true)) {

            unlockedFlowable.subscribe { event ->
                run {
                    log.info("unlockedFlowable  $event")

                    //get passwordHash from event and get password from PasswordState , then send to eth
                    val passwordHash = Util.toHexString(event.contractId)
                    val password = Util.toHexString(event.preimage)
                    val passwordMessage = PasswordMessage(passwordHash, password)
                    callTransferRequestFlow(passwordMessage)
                }
            }
        }
    }

    //corda payee -> eth payee -> eth payer
    fun sendPasswordHash(passwordHashMessage: String): String {
        //send passwordHash
        val byteArray = Util.hexToByteArray(passwordHashMessage)
        val transactionReceipt = payeeHtlcContract!!.depositFunds(payerETHAccount.address, byteArray, BigInteger.valueOf(600L), tokenContract, BigInteger.ONE).send()
        log.info("sendPasswordHash: $transactionReceipt")
        return "";

    }

    //corda payer -> eth payer
    fun sendPassword(passwordHash: String): String {
        val passwordHashRequestIdCriteria = QueryCriteria.VaultCustomQueryCriteria(
                PasswordSchemaV1.PersistentPassword::passwordHash.equal(passwordHash)
        )

        val passwordState = serviceHub.vaultService.queryBy<PasswordState>(passwordHashRequestIdCriteria.and(passwordHashRequestIdCriteria)).states.single().state.data
        val password = passwordState.password

        val passwordHash = passwordState.passwordHash

        //send password
        payerHtlcContract!!.withdrawFunds(Util.hexToByteArray(passwordHash), Util.hexToByteArray(password))
        return "";

    }

    fun callTransferRequestFlow(passwordMessage: PasswordMessage){

        val appServiceHub = serviceHub  as AppServiceHub
        appServiceHub.startFlow(TransferRequestFlow.TransferRequest(passwordMessage))

    }


}