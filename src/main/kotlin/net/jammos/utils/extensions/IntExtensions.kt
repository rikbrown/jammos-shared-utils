package net.jammos.utils.extensions

import java.time.Duration

val Int.minutes: Duration
    get() = Duration.ofMinutes(this.toLong())

fun Int.toHexString() = Integer.toHexString(this)
fun Int.toHexString(width: Int) = String.format("0x%0${width}X", this)