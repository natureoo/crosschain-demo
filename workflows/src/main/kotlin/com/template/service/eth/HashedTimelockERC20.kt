package com.template.service.eth

import io.reactivex.Flowable
import org.web3j.abi.EventEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.RemoteFunctionCall
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.BaseEventResponse
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tuples.generated.Tuple9
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger
import java.util.*
import java.util.concurrent.Callable

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
class HashedTimelockERC20 : Contract {
    @Deprecated("")
    protected constructor(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?) : super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit) {
    }

    protected constructor(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?) : super(BINARY, contractAddress, web3j, credentials, contractGasProvider) {}
    @Deprecated("")
    protected constructor(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?) : super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit) {
    }

    protected constructor(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?) : super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider) {}

    fun withdrawFunds(_contractId: ByteArray?, _preimage: ByteArray?): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
                FUNC_WITHDRAWFUNDS,
                Arrays.asList<Type<*>>(Bytes32(_contractId),
                        Bytes32(_preimage)), emptyList())
        return executeRemoteCallTransaction(function)
    }

    fun refund(_contractId: ByteArray?): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
                FUNC_REFUND,
                Arrays.asList<Type<*>>(Bytes32(_contractId)), emptyList())
        return executeRemoteCallTransaction(function)
    }

    fun getContract(_contractId: ByteArray?): RemoteFunctionCall<Tuple9<String, String, String, BigInteger, ByteArray, BigInteger, Boolean, Boolean, ByteArray>> {
        val function = Function(FUNC_GETCONTRACT,
                Arrays.asList<Type<*>>(Bytes32(_contractId)),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Address?>() {}, object : TypeReference<Address?>() {}, object : TypeReference<Address?>() {}, object : TypeReference<Uint256?>() {}, object : TypeReference<Bytes32?>() {}, object : TypeReference<Uint256?>() {}, object : TypeReference<Bool?>() {}, object : TypeReference<Bool?>() {}, object : TypeReference<Bytes32?>() {}))
        return RemoteFunctionCall(function,
                Callable {
                    val results = executeCallMultipleValueReturn(function)
                    Tuple9(
                            results[0].value as String,
                            results[1].value as String,
                            results[2].value as String,
                            results[3].value as BigInteger,
                            results[4].value as ByteArray,
                            results[5].value as BigInteger,
                            results[6].value as Boolean,
                            results[7].value as Boolean,
                            results[8].value as ByteArray)
                })
    }

    fun depositFunds(_receiver: String?, _hashlock: ByteArray?, _timelock: BigInteger?, _tokenContract: String?, _amount: BigInteger?): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
                FUNC_DEPOSITFUNDS,
                Arrays.asList<Type<*>>(Address(160, _receiver),
                        Bytes32(_hashlock),
                        Uint256(_timelock),
                        Address(160, _tokenContract),
                        Uint256(_amount)), emptyList())
        return executeRemoteCallTransaction(function)
    }

    fun getDepositFundsEvents(transactionReceipt: TransactionReceipt?): List<DepositFundsEventResponse> {
        val valueList = extractEventParametersWithLog(DEPOSITFUNDS_EVENT, transactionReceipt)
        val responses = ArrayList<DepositFundsEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = DepositFundsEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.contractId = eventValues.indexedValues[0].value as ByteArray
            typedResponse.sender = eventValues.indexedValues[1].value as String
            typedResponse.receiver = eventValues.indexedValues[2].value as String
            typedResponse.tokenContract = eventValues.nonIndexedValues[0].value as String
            typedResponse.amount = eventValues.nonIndexedValues[1].value as BigInteger
            typedResponse.hashlock = eventValues.nonIndexedValues[2].value as ByteArray
            typedResponse.timelock = eventValues.nonIndexedValues[3].value as BigInteger
            responses.add(typedResponse)
        }
        return responses
    }

    fun depositFundsEventFlowable(filter: EthFilter?): Flowable<DepositFundsEventResponse> {
        return web3j.ethLogFlowable(filter).map(object : io.reactivex.functions.Function<Log?, DepositFundsEventResponse?> {
            override fun apply(log: Log): DepositFundsEventResponse? {
                val eventValues = extractEventParametersWithLog(DEPOSITFUNDS_EVENT, log) ?: return null
                val typedResponse = DepositFundsEventResponse()
                typedResponse.log = log
                typedResponse.contractId = eventValues.indexedValues[0].value as ByteArray
                typedResponse.sender = eventValues.indexedValues[1].value as String
                typedResponse.receiver = eventValues.indexedValues[2].value as String
                typedResponse.tokenContract = eventValues.nonIndexedValues[0].value as String
                typedResponse.amount = eventValues.nonIndexedValues[1].value as BigInteger
                typedResponse.hashlock = eventValues.nonIndexedValues[2].value as ByteArray
                typedResponse.timelock = eventValues.nonIndexedValues[3].value as BigInteger
                return typedResponse
            }
        })
    }

    fun depositFundsEventFlowable(startBlock: DefaultBlockParameter?, endBlock: DefaultBlockParameter?): Flowable<DepositFundsEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(DEPOSITFUNDS_EVENT))
        return depositFundsEventFlowable(filter)
    }

    fun getWithdrawFundsEvents(transactionReceipt: TransactionReceipt?): List<WithdrawFundsEventResponse> {
        val valueList = extractEventParametersWithLog(WITHDRAWFUNDS_EVENT, transactionReceipt)
        val responses = ArrayList<WithdrawFundsEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = WithdrawFundsEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.contractId = eventValues.log.topics[1]
            typedResponse.receive = eventValues.log.topics[2]
            typedResponse.preimage = eventValues.log.topics[3]
            responses.add(typedResponse)
        }
        return responses
    }

    fun withdrawFundsEventFlowable(filter: EthFilter?): Flowable<WithdrawFundsEventResponse> {
        return web3j.ethLogFlowable(filter).map(object : io.reactivex.functions.Function<Log?, WithdrawFundsEventResponse> {
            override fun apply(log: Log): WithdrawFundsEventResponse {
                val eventValues = extractEventParametersWithLog(WITHDRAWFUNDS_EVENT, log)
                val typedResponse = WithdrawFundsEventResponse()
                typedResponse.log = log
                typedResponse.contractId = eventValues.log.topics[1]
                typedResponse.receive = eventValues.log.topics[2]
                typedResponse.preimage = eventValues.log.topics[3]
                return typedResponse
            }
        })
    }

    fun withdrawFundsEventFlowable(startBlock: DefaultBlockParameter?, endBlock: DefaultBlockParameter?): Flowable<WithdrawFundsEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(WITHDRAWFUNDS_EVENT))
        return withdrawFundsEventFlowable(filter)
    }

    fun getRefundEvents(transactionReceipt: TransactionReceipt?): List<RefundEventResponse> {
        val valueList = extractEventParametersWithLog(REFUND_EVENT, transactionReceipt)
        val responses = ArrayList<RefundEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = RefundEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.contractId = eventValues.indexedValues[0].value as ByteArray
            responses.add(typedResponse)
        }
        return responses
    }

    fun refundEventFlowable(filter: EthFilter?): Flowable<RefundEventResponse> {
        return web3j.ethLogFlowable(filter).map(object : io.reactivex.functions.Function<Log?, RefundEventResponse> {
            override fun apply(log: Log): RefundEventResponse {
                val eventValues = extractEventParametersWithLog(REFUND_EVENT, log)
                val typedResponse = RefundEventResponse()
                typedResponse.log = log
                typedResponse.contractId = eventValues.indexedValues[0].value as ByteArray
                return typedResponse
            }
        })
    }

    fun refundEventFlowable(startBlock: DefaultBlockParameter?, endBlock: DefaultBlockParameter?): Flowable<RefundEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(REFUND_EVENT))
        return refundEventFlowable(filter)
    }

    class DepositFundsEventResponse : BaseEventResponse() {
        var contractId: ByteArray = ByteArray(32)
        var sender: String = ""
        var receiver: String = ""
        var tokenContract: String = ""
        var amount: BigInteger = BigInteger.ZERO
        var hashlock: ByteArray = ByteArray(32)
        var timelock: BigInteger = BigInteger.ZERO
    }

    class WithdrawFundsEventResponse : BaseEventResponse() {
        var contractId: String = ""

        var receive: String = ""

        var preimage: String = ""
    }

    class RefundEventResponse : BaseEventResponse() {
        var contractId: ByteArray? = null
    }

    companion object {
        const val BINARY = "608060405234801561001057600080fd5b50610e73806100206000396000f3fe608060405234801561001057600080fd5b5060043610610068577c0100000000000000000000000000000000000000000000000000000000600035046318492006811461006d5780637249fbb6146100a4578063e16c7d98146100c1578063fb334df714610138575b600080fd5b6100906004803603604081101561008357600080fd5b508035906020013561018c565b604080519115158252519081900360200190f35b610090600480360360208110156100ba57600080fd5b503561055d565b6100de600480360360208110156100d757600080fd5b5035610852565b60408051600160a060020a039a8b168152988a1660208a015296909816878701526060870194909452608086019290925260a0850152151560c0840152151560e08301526101008201929092529051908190036101200190f35b61017a600480360360a081101561014e57600080fd5b50600160a060020a03813581169160208101359160408201359160608101359091169060800135610900565b60408051918252519081900360200190f35b60008261019881610da1565b15156101ee576040805160e560020a62461bcd02815260206004820152601960248201527f636f6e7472616374496420646f6573206e6f7420657869737400000000000000604482015290519081900360640190fd5b8383600281604051602001808281526020019150506040516020818303038152906040526040518082805190602001908083835b602083106102415780518252601f199092019160209182019101610222565b51815160209384036101000a60001901801990921691161790526040519190930194509192505080830381855afa158015610280573d6000803e3d6000fd5b5050506040513d602081101561029557600080fd5b5051600083815260208190526040902060040154146102fe576040805160e560020a62461bcd02815260206004820152601c60248201527f686173686c6f636b206861736820646f6573206e6f74206d6174636800000000604482015290519081900360640190fd5b6000868152602081905260409020600101548690600160a060020a03163314610371576040805160e560020a62461bcd02815260206004820152601a60248201527f776974686472617761626c653a206e6f74207265636569766572000000000000604482015290519081900360640190fd5b60008181526020819052604090206006015460ff16156103db576040805160e560020a62461bcd02815260206004820152601f60248201527f776974686472617761626c653a20616c72656164792077697468647261776e00604482015290519081900360640190fd5b600081815260208190526040902060060154610100900460ff161561044a576040805160e560020a62461bcd02815260206004820152601e60248201527f776974686472617761626c653a20616c726561647920726566756e6465640000604482015290519081900360640190fd5b600087815260208181526040808320600781018a905560068101805460ff19166001908117909155600282015490820154600383015484517fa9059cbb000000000000000000000000000000000000000000000000000000008152600160a060020a03928316600482015260248101919091529351929591169363a9059cbb936044808201949293918390030190829087803b1580156104e957600080fd5b505af11580156104fd573d6000803e3d6000fd5b505050506040513d602081101561051357600080fd5b5050604080513381526020810189905281518a927f4151ddecd8a73ecfb7e8c7f6bc6e7f28d15a1c18eb245586c942e9d96a8cf811928290030190a2506001979650505050505050565b60008161056981610da1565b15156105bf576040805160e560020a62461bcd02815260206004820152601960248201527f636f6e7472616374496420646f6573206e6f7420657869737400000000000000604482015290519081900360640190fd5b6000838152602081905260409020548390600160a060020a0316331461062f576040805160e560020a62461bcd02815260206004820152601660248201527f726566756e6461626c653a206e6f742073656e64657200000000000000000000604482015290519081900360640190fd5b600081815260208190526040902060060154610100900460ff161561069e576040805160e560020a62461bcd02815260206004820152601c60248201527f726566756e6461626c653a20616c726561647920726566756e64656400000000604482015290519081900360640190fd5b60008181526020819052604090206006015460ff1615610708576040805160e560020a62461bcd02815260206004820152601d60248201527f726566756e6461626c653a20616c72656164792077697468647261776e000000604482015290519081900360640190fd5b60008181526020819052604090206005015442101561075b5760405160e560020a62461bcd028152600401808060200182810382526023815260200180610de26023913960400191505060405180910390fd5b60008481526020818152604080832060068101805461ff00191661010017905560028101548154600383015484517fa9059cbb000000000000000000000000000000000000000000000000000000008152600160a060020a03928316600482015260248101919091529351929591169363a9059cbb936044808201949293918390030190829087803b1580156107f057600080fd5b505af1158015610804573d6000803e3d6000fd5b505050506040513d602081101561081a57600080fd5b505060405185907f3fbd469ec3a5ce074f975f76ce27e727ba21c99176917b97ae2e713695582a1290600090a2506001949350505050565b60008060008060008060008060006108698a610da1565b151561088f575060009750879650869550859450849350839250829150819050806108f3565b50505060008781526020819052604090208054600182015460028301546003840154600485015460058601546006870154600790970154600160a060020a039687169d509486169b509490921698509650945090925060ff80831692610100900416905b9193959799909294969850565b600082338383811161095c576040805160e560020a62461bcd02815260206004820152601860248201527f746f6b656e20616d6f756e74206d757374206265203e20300000000000000000604482015290519081900360640190fd5b604080517fdd62ed3e000000000000000000000000000000000000000000000000000000008152600160a060020a0384811660048301523060248301529151839286169163dd62ed3e916044808301926020929190829003018186803b1580156109c557600080fd5b505afa1580156109d9573d6000803e3d6000fd5b505050506040513d60208110156109ef57600080fd5b50511015610a315760405160e560020a62461bcd028152600401808060200182810382526021815260200180610e056021913960400191505060405180910390fd5b8660008111610a745760405160e560020a62461bcd028152600401808060200182810382526023815260200180610dbf6023913960400191505060405180910390fd5b889450428801610a8386610da1565b15610ad8576040805160e560020a62461bcd02815260206004820152601760248201527f436f6e747261637420616c726561647920657869737473000000000000000000604482015290519081900360640190fd5b604080517f23b872dd000000000000000000000000000000000000000000000000000000008152336004820152306024820152604481018990529051600160a060020a038a16916323b872dd9160648083019260209291908290030181600087803b158015610b4657600080fd5b505af1158015610b5a573d6000803e3d6000fd5b505050506040513d6020811015610b7057600080fd5b50511515610bb25760405160e560020a62461bcd028152600401808060200182810382526022815260200180610e266022913960400191505060405180910390fd5b6101206040519081016040528033600160a060020a031681526020018c600160a060020a0316815260200189600160a060020a031681526020018881526020018b81526020018a8152602001600015158152602001600015158152602001600060010281525060008088815260200190815260200160002060008201518160000160006101000a815481600160a060020a030219169083600160a060020a0316021790555060208201518160010160006101000a815481600160a060020a030219169083600160a060020a0316021790555060408201518160020160006101000a815481600160a060020a030219169083600160a060020a03160217905550606082015181600301556080820151816004015560a0820151816005015560c08201518160060160006101000a81548160ff02191690831515021790555060e08201518160060160016101000a81548160ff02191690831515021790555061010082015181600701559050508a600160a060020a031633600160a060020a0316877f0b058ff4168d86780241d0ea96a32a0dece6001f93c970388ed124fff490b6ee8b8b8f8f6040518085600160a060020a0316600160a060020a0316815260200184815260200183815260200182815260200194505050505060405180910390a4505050505095945050505050565b600090815260208190526040902054600160a060020a031615159056fe74696d656c6f636b2074696d65206d75737420626520696e2074686520667574757265726566756e6461626c653a2074696d656c6f636b206e6f742079657420706173736564746f6b656e20616c6c6f77616e6365206d757374206265203e3d20616d6f756e747472616e7366657246726f6d2073656e64657220746f2074686973206661696c6564a165627a7a72305820a21a6edbdfa1aa30948f96cbd2fe40f7c47935068543d4be6d248383182a12880029"
        const val FUNC_WITHDRAWFUNDS = "withdrawFunds"
        const val FUNC_REFUND = "refund"
        const val FUNC_GETCONTRACT = "getContract"
        const val FUNC_DEPOSITFUNDS = "depositFunds"
        val DEPOSITFUNDS_EVENT = Event("DepositFunds",
                Arrays.asList<TypeReference<*>>(object : TypeReference<Bytes32?>(true) {}, object : TypeReference<Address?>(true) {}, object : TypeReference<Address?>(true) {}, object : TypeReference<Address?>() {}, object : TypeReference<Uint256?>() {}, object : TypeReference<Bytes32?>() {}, object : TypeReference<Uint256?>() {}))
        val WITHDRAWFUNDS_EVENT = Event("WithdrawFunds",
                Arrays.asList<TypeReference<*>>(object : TypeReference<Bytes32?>(true) {}, object : TypeReference<Address?>() {}, object : TypeReference<Bytes32?>() {}))
        val REFUND_EVENT = Event("Refund",
                Arrays.asList<TypeReference<*>>(object : TypeReference<Bytes32?>(true) {}))

        @Deprecated("")
        fun load(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?): HashedTimelockERC20 {
            return HashedTimelockERC20(contractAddress, web3j, credentials, gasPrice, gasLimit)
        }

        @Deprecated("")
        fun load(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?): HashedTimelockERC20 {
            return HashedTimelockERC20(contractAddress, web3j, transactionManager, gasPrice, gasLimit)
        }

        fun load(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?): HashedTimelockERC20 {
            return HashedTimelockERC20(contractAddress, web3j, credentials, contractGasProvider)
        }

        fun load(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?): HashedTimelockERC20 {
            return HashedTimelockERC20(contractAddress, web3j, transactionManager, contractGasProvider)
        }

        fun deploy(web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?): RemoteCall<HashedTimelockERC20> {
            return deployRemoteCall(HashedTimelockERC20::class.java, web3j, credentials, contractGasProvider, BINARY, "")
        }

        @Deprecated("")
        fun deploy(web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?): RemoteCall<HashedTimelockERC20> {
            return deployRemoteCall(HashedTimelockERC20::class.java, web3j, credentials, gasPrice, gasLimit, BINARY, "")
        }

        fun deploy(web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?): RemoteCall<HashedTimelockERC20> {
            return deployRemoteCall(HashedTimelockERC20::class.java, web3j, transactionManager, contractGasProvider, BINARY, "")
        }

        @Deprecated("")
        fun deploy(web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?): RemoteCall<HashedTimelockERC20> {
            return deployRemoteCall(HashedTimelockERC20::class.java, web3j, transactionManager, gasPrice, gasLimit, BINARY, "")
        }
    }
}