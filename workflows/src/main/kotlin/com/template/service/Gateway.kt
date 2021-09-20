package com.template.service

/**
 * @author nature
 * @date 4/9/21 上午10:57
 * @email 924943578@qq.com
 */
import com.template.metadata.ETHAccount
import com.template.metadata.PasswordMessage
import com.template.metadata.Role
import com.template.schema.PasswordSchemaV1
import com.template.service.eth.HashedTimelockERC20
import com.template.states.PasswordState
import com.template.tool.Util
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.node.services.queryBy
import net.corda.core.node.services.vault.Builder.equal
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.utilities.loggerFor
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.quorum.Quorum
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger


@CordaService
//class Gateway(val serviceHub: AppServiceHub) : SingletonSerializeAsToken() {
class Gateway(val serviceHub: ServiceHub) : SingletonSerializeAsToken() {


    val ourIdentity = serviceHub.myInfo.legalIdentities.first()
    val ourName = ourIdentity.name.toString()

    val URL = "https://rinkeby.infura.io/v3/b8a7ec5d22c14bd6b2ffca2352395ed4"

    val contractAddress = "0x5E5A4262Bc1254729EcBa99b6Aa03257a627dd32"

    val tokenContract = "0x69b726d0cec2c6d9026473b3e7ce91e71b58e70e"

    //22000000000L
    val GAS_PRICE = BigInteger.valueOf(22000000000L)

    //4300000L
    val GAS_LIMIT = BigInteger.valueOf(7300000L)

    val timeLock = BigInteger.valueOf(3600L) //3600s

    val ethAmount = BigInteger.valueOf(10L)

    //account A
    val payerETHAccount = ETHAccount(Role.PAYER,"0x3761940D8aDd75AC0A2f37670a62A71c905475b5", "0xD8227ed611b855D74D1D0F60Da995956306c5577","2d5829a087b9c70d35965ba6357d2269692b6d070df1b941571d9fd1b8b841c4")

    //account B
    val payeeETHAccount = ETHAccount(Role.PAYEE,"0xD8227ed611b855D74D1D0F60Da995956306c5577", "0x3761940D8aDd75AC0A2f37670a62A71c905475b5","f279649ae6910401d77acb4e4e204244f93410429a0af02967fc110f4471f6ae")


    var myETHAccount: ETHAccount? = null

    var myHtlcContract: HashedTimelockERC20? = null




    private companion object {
        val log = loggerFor<Gateway>()

    }



    init {
//        initWeb3jTest()
//        initHttpTest()
        initWeb3j()
        log.info("Gateway init")
    }

//    private fun request() =
//            Request.Builder().url("https://www.google.com.hk/webhp?hl=zh-CN&sourceid=cnhp&gws_rd=ssl").build()


//    fun initHttpTest(){
//        val client = OkHttpClient()
//        val response = client.newCall(request()).execute()
//        println(response)
//
//    }

    fun initWeb3jTest() {

        try {
            val RPC_URL = "https://rinkeby.infura.io/v3/b8a7ec5d22c14bd6b2ffca2352395ed4";
            val web3j = Quorum.build(HttpService(RPC_URL))

            val contractAddress = "0x5E5A4262Bc1254729EcBa99b6Aa03257a627dd32"

            val aAddress = "0x3761940D8aDd75AC0A2f37670a62A71c905475b5" //A

            val bAddress = "0xD8227ed611b855D74D1D0F60Da995956306c5577" //B


            val tokenContract = "0x69b726d0cec2c6d9026473b3e7ce91e71b58e70e"

            val aCredentials = Credentials.create("2d5829a087b9c70d35965ba6357d2269692b6d070df1b941571d9fd1b8b841c4") //B

            val bCredentials = Credentials.create("f279649ae6910401d77acb4e4e204244f93410429a0af02967fc110f4471f6ae") //B


            val aHtlcContract = HashedTimelockERC20.load(
                    contractAddress, web3j, aCredentials, StaticGasProvider(GAS_PRICE, GAS_LIMIT))

            val bHtlcContract = HashedTimelockERC20.load(
                    contractAddress, web3j, bCredentials, StaticGasProvider(GAS_PRICE, GAS_LIMIT))


            //(String _receiver, byte[] _hashlock, BigInteger _timelock, String _tokenContract, BigInteger _amount)
            //(String _receiver, byte[] _hashlock, BigInteger _timelock, String _tokenContract, BigInteger _amount)
            val passwordHashBytes = Util.hexToByteArray("0x40e254db30ad0c68cafbefb8bf2bf7fc101b96ff1dcd8182429c36179964aff8")


            //corda payer-> corda payee -> eth b -> eth a -> corda payer (locked asset event) -> eth a (password) -> eth b (password) -> corda payee (unlocked event with password)
            //corda payer-> corda payee -> eth b -> eth a -> corda payer (locked asset event) -> eth a (password) -> eth b (password) -> corda payee (unlocked event with password)
            val transactionReceipt = bHtlcContract.depositFunds(aAddress, passwordHashBytes, BigInteger.valueOf(600L),
                    tokenContract, BigInteger("1000000")).send()
            println(transactionReceipt)
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    fun initWeb3j() {

        val web3j = Quorum.build(HttpService(URL))

        if(ourName != null && ourName.contains("payer", true)) {
            log.info("Role: payer")
            myETHAccount = payerETHAccount
        }
        else if(ourName != null && ourName.contains("payee", true)) {
            log.info("Role: payee")
            myETHAccount = payeeETHAccount
        }
        else{
            log.info("Role: none")
            return;
        }


        log.info("myETHAccount: $myETHAccount")

        myHtlcContract = HashedTimelockERC20.load(
                contractAddress, web3j, Credentials.create(myETHAccount!!.privateKey), StaticGasProvider(GAS_PRICE, GAS_LIMIT))

        log.info("myHtlcContract: $myHtlcContract")


        //payer listen locked event
        if(myETHAccount!!.role == Role.PAYER) {
            //Receive locked asset event -> send password to eth
            val lockedAssetFlowable = myHtlcContract!!.depositFundsEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST);
            lockedAssetFlowable.subscribe { event ->
                run {
                    log.info("lockedAssetFlowable:  $event")

                    //get passwordHash from event and get password from PasswordState , then send to eth

                    var passworHash = Util.toHexString(event.hashlock)

                    log.info("lockedAssetFlowable:  $passworHash")

                    sendPassword(passworHash)
                }
            }
        }




        //payee listen unlocked event
        if(myETHAccount!!.role == Role.PAYEE) {
            //Receive unlocked asset event -> trigger TransferRequest flow

            val unlockedAssetFlowable = myHtlcContract!!.withdrawFundsEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST);

            unlockedAssetFlowable.subscribe { event ->
                run {
                    log.info("unlockedFlowable  $event")

                    //get passwordHash from event and get password from PasswordState , then send to eth
                    val passwordHash = event.contractId
                    val password = event.preimage
                    val passwordMessage = PasswordMessage(passwordHash, password)
                    callTransferRequestFlow(passwordMessage)
                }
            }
        }
    }

    //corda payee -> eth payee -> eth payer
    fun sendPasswordHash(passwordHashMessage: String): String {
        //send passwordHash
        try {
            val byteArray = Util.hexToByteArray(passwordHashMessage)
            val transactionReceipt = myHtlcContract!!.depositFunds(myETHAccount!!.toAddress, byteArray, timeLock, tokenContract, ethAmount).send()
            log.info("sendPasswordHash depositFunds  transactionReceipt: $transactionReceipt")
        }catch(e: Exception){
            log.error(e.toString())
        }
        return "";

    }

    //corda payer -> eth payer
    fun sendPassword(passwordHash: String): String {
        try {
            val passwordHashRequestIdCriteria = QueryCriteria.VaultCustomQueryCriteria(
                    PasswordSchemaV1.PersistentPassword::passwordHash.equal(passwordHash)
            )

            val passwordState = serviceHub.vaultService.queryBy<PasswordState>(passwordHashRequestIdCriteria.and(passwordHashRequestIdCriteria)).states.single().state.data

            log.info("sendPassword passwordState: $passwordState")

            val password = passwordState.password

            log.info("sendPassword password: $password")

            //send password
            val transactionReceipt = myHtlcContract!!.withdrawFunds(Util.hexToByteArray(passwordHash), Util.hexToByteArray(password)).send()

            log.info("sendPassword withdrawFunds  transactionReceipt: $transactionReceipt")

        }catch(e: Exception){
            log.error(e.toString())
        }
        return "";

    }

    fun callTransferRequestFlow(passwordMessage: PasswordMessage){
//        try {
//            val appServiceHub = serviceHub as AppServiceHub
//            appServiceHub.startFlow(TransferRequestFlow.TransferRequest(passwordMessage))
//        }catch(e: Exception){
//            log.error(e.toString())
//        }

    }


}