package com.template.tool

import org.web3j.utils.Numeric
import java.security.MessageDigest
import java.util.*


/**
 * @author nature
 * @date 9/9/21 下午7:14
 * @email 924943578@qq.com
 */
class Util {

    companion object {
        fun generatePassword(): String {
//            val bytes = UUID.randomUUID().toString().toByteArray()
//            return toHexString(bytes)

            val bytes = ByteArray(32)
            Random().nextBytes(bytes)
            return toHexString(bytes)
        }


        fun generateHash(input: String): String {
            val inputBytes = hexToByteArray(input)
            val md = MessageDigest.getInstance("SHA-256")
            val outputBytes = md.digest(inputBytes)
            return toHexString(outputBytes)
        }

        fun toHexString(array: ByteArray): String {
            return Numeric.toHexString(array)
        }


        fun hexToByteArray(input: String): ByteArray {
            val cleanInput: String = Numeric.cleanHexPrefix(input)
            val len = cleanInput.length
            if (len == 0) {
                return byteArrayOf()
            }
            val data: ByteArray
            val startIdx: Int
            if (len % 2 != 0) {
                data = ByteArray(len / 2 + 1)
                data[0] = Character.digit(cleanInput[0], 16).toByte()
                startIdx = 1
            } else {
                data = ByteArray(len / 2)
                startIdx = 0
            }
            var i = startIdx
            while (i < len) {
                data[(i + 1) / 2] = ((Character.digit(cleanInput[i], 16) shl 4)
                        + Character.digit(cleanInput[i + 1], 16)).toByte()
                i += 2
            }
            return data
        }
    }
}