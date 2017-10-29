package net.jammos.utils

object ByteArrays {
    fun randomBytes(count: Int): ByteArray {

        val bytes = ByteArray(count)
        RANDOM.nextBytes(bytes)
        return bytes
    }

    fun randomSeed(count: Int): ByteArray = RANDOM.generateSeed(count)
}