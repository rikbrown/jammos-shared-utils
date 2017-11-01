package net.jammos.utils.extensions

import com.google.common.net.InetAddresses
import net.jammos.utils.types.BigUnsignedInteger
import java.io.DataInput
import java.io.DataOutput
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Read [cnt] chars from the input as a [String]
 */
fun DataInput.readChars(cnt: Int, reverse: Boolean = true): String {
    val bytes = readBytes(cnt)
    val string = String(bytes).trimEnd { c -> c == Character.MIN_VALUE }
    return if (reverse) string.reversed() else string
}

fun DataInput.readChars(until: Byte = 0, reverse: Boolean = true): String {
    val sb = StringBuilder()
    while (true) {
        val byte = readByte()
        if (byte == until) {
            break
        }
        sb.append(byte.toChar())
    }
    return if (reverse) sb.toString().reversed() else sb.toString()
}

fun DataOutput.writeCharsWithTerminator(string: String, terminator: Int = 0) {
    this.write(string.toByteArray(Charsets.US_ASCII))
    this.writeByte(terminator)
}

/**
 * Reads 4 bytes from the input as an [Int] (UInt32)
 */
fun DataInput.readUnsignedInt(): Int {
    val bytes = readBytes(4)
    return ByteBuffer.wrap(bytes).int
}

fun DataInput.readUnsignedIntLe(): Int {
    val bytes = readBytes(4)
    return ByteBuffer
            .wrap(bytes)
            .order(ByteOrder.LITTLE_ENDIAN)
            .int
}

fun DataInput.readIpAddress(): InetAddress {
    /** [InetAddresses.fromInteger] because [InetAddresses.fromLittleEndianByteArray] forces little endian].
     * and we use this because [InetAddress] can potentially make network calls on construction...
     */
    return InetAddresses.fromInteger(readInt())
}

fun DataInput.readBytes(cnt: Int): ByteArray {
    val buffer = ByteArray(cnt)
    readFully(buffer)
    return buffer
}

fun DataInput.readBigUnsigned(size: Int): BigUnsignedInteger {
    val bytes = readBytes(size)
    return BigUnsignedInteger(bytes)
}
