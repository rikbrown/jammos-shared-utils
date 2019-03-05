package net.jammos.utils.realm

import net.jammos.utils.types.BigUnsignedInteger

enum class RealmType(val value: BigUnsignedInteger) {
    NORMAL(BigUnsignedInteger(0)),
    PVP(BigUnsignedInteger(1)),
    RP(BigUnsignedInteger(6)),
    RPPVP(BigUnsignedInteger(8))
}

