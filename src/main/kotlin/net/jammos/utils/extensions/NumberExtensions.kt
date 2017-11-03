package net.jammos.utils.extensions

import java.time.Duration

val Number.minutes: Duration
    get() = Duration.ofMinutes(this.toLong())

fun Number.toHexString() = Integer.toHexString(this.toInt()) // FIXME later
fun Number.toHexString(width: Int) = String.format("0x%0${width}X", this)