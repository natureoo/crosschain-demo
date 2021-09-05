package com.template.service

/**
 * @author nature
 * @date 4/9/21 上午10:57
 * @email 924943578@qq.com
 */
import com.template.flows.TransferRequestFlow
import com.template.metadata.PasswordMessage
import net.corda.core.node.AppServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.serialization.SingletonSerializeAsToken



@CordaService
class Gateway(val services: AppServiceHub) : SingletonSerializeAsToken() {


    private val myKey = services.myInfo.legalIdentities.first().owningKey

//    fun sendMessage(passwordHashMessage: PasswordHashMessage): Int {
    fun sendMessage(passwordHashMessage: String): String {

        return "";

    }

    fun callTransferRequest(passwordMessage: PasswordMessage){

        services.startFlow(TransferRequestFlow.TransferRequest(passwordMessage))

    }


}