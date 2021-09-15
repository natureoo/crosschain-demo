package com.template.utils

import org.web3j.utils.Numeric
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

//        fun toHexString(array: ByteArray): String {
//            return DatatypeConverter.printHexBinary(array)
//        }
//
//        fun hexToByteArray(s: String): ByteArray {
//            return DatatypeConverter.parseHexBinary(s)
//        }


        //    public static String toHexString(byte[] array) {
//        return DatatypeConverter.printHexBinary(array);
//    }
//
//    public static byte[] hexToByteArray(String s) {
//        return DatatypeConverter.parseHexBinary(s);
//    }
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