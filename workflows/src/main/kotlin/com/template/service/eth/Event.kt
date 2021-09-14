package com.template.service.eth

import io.reactivex.Flowable
import org.web3j.abi.EventEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Utf8String
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.RemoteFunctionCall
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.BaseEventResponse
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger
import java.util.*

/**
 *
 * Auto generated code.
 *
 * **Do not modify!**
 *
 * Please use the [web3j command line tools](https://docs.web3j.io/command_line.html),
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * [codegen module](https://github.com/web3j/web3j/tree/master/codegen) to update.
 *
 *
 * Generated with web3j version 1.4.1.
 */
class Event : Contract {
    @Deprecated("")
    protected constructor(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?) : super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit) {
    }

    protected constructor(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?) : super(BINARY, contractAddress, web3j, credentials, contractGasProvider) {}
    @Deprecated("")
    protected constructor(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?) : super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit) {
    }

    protected constructor(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?) : super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider) {}

    fun getDataStoredEvents(transactionReceipt: TransactionReceipt?): List<DataStoredEventResponse> {
        val valueList = extractEventParametersWithLog(DATASTORED_EVENT, transactionReceipt)
        val responses = ArrayList<DataStoredEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = DataStoredEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.data1 = eventValues.nonIndexedValues[0].value as String
            typedResponse.data2 = eventValues.nonIndexedValues[1].value as String
            responses.add(typedResponse)
        }
        return responses
    }

    fun dataStoredEventFlowable(filter: EthFilter): Flowable<DataStoredEventResponse> {
        return web3j.ethLogFlowable(filter).map { log ->
            val eventValues = extractEventParametersWithLog(DATASTORED_EVENT, log)
            val typedResponse = DataStoredEventResponse()
            typedResponse.log = log
            typedResponse.data1 = eventValues.nonIndexedValues[0].value as String
            typedResponse.data2 = eventValues.nonIndexedValues[1].value as String
            typedResponse
        }
    }

    fun dataStoredEventFlowable(startBlock: DefaultBlockParameter?, endBlock: DefaultBlockParameter?): Flowable<DataStoredEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(DATASTORED_EVENT))
        return dataStoredEventFlowable(filter)
    }

    fun storeData(): RemoteFunctionCall<TransactionReceipt> {
        val function = org.web3j.abi.datatypes.Function(
                FUNC_STOREDATA,
                Arrays.asList(), emptyList())
        return executeRemoteCallTransaction(function)
    }

    class DataStoredEventResponse : BaseEventResponse() {
        var data1: String? = null
        var data2: String? = null
    }

    companion object {
        const val BINARY = "608060405234801561001057600080fd5b5060cf8061001f6000396000f3fe6080604052348015600f57600080fd5b506004361060285760003560e01c80634abe305214602d575b600080fd5b60336035565b005b7f4d29b7f0ee29fd19bc5c40e7a45531c8498ce1293f4c3f7f42518934235f36ed604051608f9060408082526001908201819052606160f81b6060830152608060208301819052820152603160f91b60a082015260c00190565b60405180910390a156fea2646970667358221220a384c68791afd5f52ffed7c494255bd200b49f5f3b0fe9ff79146fe4a9af21ec64736f6c63430008060033"
        const val FUNC_STOREDATA = "storeData"
        val DATASTORED_EVENT = org.web3j.abi.datatypes.Event("DataStored",
                Arrays.asList<TypeReference<*>>(object : TypeReference<Utf8String?>() {}, object : TypeReference<Utf8String?>() {}))

        @Deprecated("")
        fun load(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?): Event {
            return Event(contractAddress, web3j, credentials, gasPrice, gasLimit)
        }

        @Deprecated("")
        fun load(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?): Event {
            return Event(contractAddress, web3j, transactionManager, gasPrice, gasLimit)
        }

        fun load(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?): Event {
            return Event(contractAddress, web3j, credentials, contractGasProvider)
        }

        fun load(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?): Event {
            return Event(contractAddress, web3j, transactionManager, contractGasProvider)
        }

        fun deploy(web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?): RemoteCall<Event> {
            return deployRemoteCall(Event::class.java, web3j, credentials, contractGasProvider, BINARY, "")
        }

        @Deprecated("")
        fun deploy(web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?): RemoteCall<Event> {
            return deployRemoteCall(Event::class.java, web3j, credentials, gasPrice, gasLimit, BINARY, "")
        }

        fun deploy(web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?): RemoteCall<Event> {
            return deployRemoteCall(Event::class.java, web3j, transactionManager, contractGasProvider, BINARY, "")
        }

        @Deprecated("")
        fun deploy(web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?): RemoteCall<Event> {
            return deployRemoteCall(Event::class.java, web3j, transactionManager, gasPrice, gasLimit, BINARY, "")
        }
    }
}