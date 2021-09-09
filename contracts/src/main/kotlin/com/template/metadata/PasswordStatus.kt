package com.template.metadata

import net.corda.core.serialization.CordaSerializable

/**
 * @author nature
 * @date 3/9/21 下午12:02
 * @email 924943578@qq.com
 */
@CordaSerializable
enum class PasswordHashStatus {
    ACTIVE,
    INACTIVE
}