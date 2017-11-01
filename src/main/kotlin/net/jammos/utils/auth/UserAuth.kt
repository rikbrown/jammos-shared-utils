package net.jammos.utils.auth

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonCreator.Mode.DELEGATING
import com.fasterxml.jackson.annotation.JsonValue
import net.jammos.utils.types.BigUnsignedInteger
import net.jammos.utils.types.ComparableByteArray
import java.nio.charset.Charset
import java.time.Instant

data class UserAuth(
        val userId: UserId,
        val username: Username,
        val salt: SaltByteArray,
        val verifier: BigUnsignedInteger)

class SaltByteArray(bytes: ByteArray): ComparableByteArray(bytes)

data class UserId @JsonCreator(mode=DELEGATING) constructor(@JsonValue val userId: String) {
    override fun toString() = userId
}

data class Username @JsonCreator(mode=DELEGATING) private constructor(@JsonValue val username: String) {
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