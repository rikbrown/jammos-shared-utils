package net.jammos.utils.types

import java.nio.charset.Charset

data class InternetAddress(
        val address: String,
        val port: Int) {

    override fun toString() = "$address:$port"

    fun toByteArray(charset: Charset): ByteArray {
        return toString().toByteArray(charset)
    }

}

