package net.jammos.utils.extensions

import com.google.common.net.InetAddresses
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import net.jammos.utils.types.BigUnsignedInteger
import net.jammos.utils.types.WriteableByte
import java.io.DataInput
import java.io.DataOutput
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder


fun ByteBuf.asDataOutput(): DataOutput {
    return ByteBufOutputStream(this)
}

fun ByteBuf.asDataInput(): DataInput {
    return ByteBufInputStream(this)
}

fun ByteBuf.readCharsUntil(until: Byte): String {
    val sb = StringBuilder()
    while (true) {
        val byte = readByte()
        if (byte == until) {
            break
        }
        sb.append(byte.toChar())
    }
    return sb.toString()
    //return if (reverse) sb.toString().reversed() else sb.toString()
}

fun ByteBuf.readCharSequence(cnt: Int) = readCharSequence(cnt, Charsets.US_ASCII)

fun ByteBuf.readByteArray(count: Int): ByteArray {
    val bytes = ByteArray(count)
    readBytes(bytes)
    return bytes
}

fun ByteBuf.readIpAddress(): InetAddress {
    /** [InetAddresses.fromInteger] because [InetAddresses.fromLittleEndianByteArray] forces little endian].
     * and we use this because [InetAddress] can potentially make network calls on construction...
     */
    return InetAddresses.fromInteger(readInt())
}

fun ByteBuf.readBigUnsigned(size: Int): BigUnsignedInteger {
    val bytes = readByteArray(size)
    return BigUnsignedInteger(bytes)
}

fun ByteBuf.writeByte(writeableByte: WriteableByte) = writeByte(writeableByte.value)

fun ByteBuf.writeByte(short: Short) = writeByte(short.toInt()) // FIXME: write better

fun ByteBuf.writeCharSequenceTerminated(string: String, terminator: Int = 0) {
    writeBytes(string.toByteArray(Charsets.US_ASCII))
    writeByte(terminator)
}

