package com.template.utils

import java.math.BigInteger
import java.security.MessageDigest

/**
 * @author nature
 * @date 9/9/21 下午7:14
 * @email 924943578@qq.com
 */
class Util {

    companion object {
        fun getHash(input: String): String {
            val md = MessageDigest.getInstance("SHA-256")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }
    }
}