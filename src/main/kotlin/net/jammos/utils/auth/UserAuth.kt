package net.jammos.utils.auth

import net.jammos.utils.types.BigUnsignedInteger
import net.jammos.utils.types.ComparableByteArray
import java.nio.charset.Charset
import java.time.Instant

data class UserAuth(
        val username: Username,
        val salt: SaltByteArray,
        val verifier: BigUnsignedInteger)

class SaltByteArray(bytes: ByteArray): ComparableByteArray(bytes)

data class Username private constructor(val username: String) {
    companion object Username {
        fun username(username: String) = Username(username.toUpperCase())
    }

    val bytes get() = toByteArray()

    private fun toByteArray(charset: Charset): ByteArray {
        return username.toByteArray(charset)
    }

    private fun toByteArray() = toByteArray(Charsets.UTF_8)

    override fun toString() = username
}

data class UserSuspension(
        val start: Instant,
        val end: Instant? = null)