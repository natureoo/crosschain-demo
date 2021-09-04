package com.template.service

/**
 * @author nature
 * @date 4/9/21 上午10:57
 * @email 924943578@qq.com
 */
import com.template.metadata.PasswordHashMessage
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.serialization.SingletonSerializeAsToken



@CordaService
class Gateway(val services: ServiceHub) : SingletonSerializeAsToken() {


    private val myKey = services.myInfo.legalIdentities.first().owningKey

    fun sendMessage(passwordHashMessage: PasswordHashMessage): Int {

        return 0;

    }



}