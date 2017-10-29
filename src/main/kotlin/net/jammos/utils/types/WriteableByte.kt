package net.jammos.utils.types

import java.io.DataOutput

interface WriteableByte {
    val value: Int

    fun write(output: DataOutput) {
        output.writeByte(value)
    }
}

