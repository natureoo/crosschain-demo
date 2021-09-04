package com.template.metadata

import net.corda.core.serialization.CordaSerializable

/**
 * @author nature
 * @date 4/9/21 上午10:40
 * @email 924943578@qq.com
 */
@CordaSerializable
data class PasswordHashMessage (

    val requestId: String,
    val passwordHash: String

)