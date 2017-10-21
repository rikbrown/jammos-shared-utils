package net.jammos.utils.types

import net.jammos.utils.extensions.writeShortLe
import java.io.DataOutput

interface WriteableByte {
    val value: Int

    fun write(output: DataOutput) {
        output.writeByte(value)
    }

    fun write16(output: DataOutput) {
        output.writeShortLe(value)
    }
}

