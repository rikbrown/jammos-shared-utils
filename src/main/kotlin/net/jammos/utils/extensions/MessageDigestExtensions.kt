package net.jammos.utils.extensions

import net.jammos.utils.types.BigUnsignedInteger
import net.jammos.utils.types.ComparableByteArray
import net.jammos.utils.types.DigestByteArray
import java.security.MessageDigest

/**
 * Update with multiple byte arrays and then return the digest results
 */
fun MessageDigest.digest(vararg byteArrays: ByteArray): ByteArray {
    byteArrays.forEach { update(it) }
    return digest()
}

/**
 * Update the message digest using the bytes of a big unsigned integer
 */
fun MessageDigest.update(i: BigUnsignedInteger) {
    update(i.bytes)
}

fun MessageDigest.update(bytes: ComparableByteArray) {
    update(bytes.bytes)
}

fun MessageDigest.digestByteArray() = DigestByteArray(digest())