package net.jammos.utils.extensions

import java.time.Duration

val Int.minutes: Duration
    get() = Duration.ofMinutes(this.toLong())