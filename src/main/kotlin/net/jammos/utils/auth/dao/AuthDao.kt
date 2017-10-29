package net.jammos.utils.auth.dao

import net.jammos.utils.auth.UserAuth
import net.jammos.utils.auth.UserSuspension
import net.jammos.utils.auth.Username
import net.jammos.utils.types.BigUnsignedInteger
import java.net.InetAddress
import java.time.Instant

interface AuthDao {
    fun getUserAuth(username: Username): UserAuth?
    fun getUserSuspension(username: Username): UserSuspension?

    fun createUser(username: Username, password: String): UserAuth

    fun suspendUser(username: Username, start: Instant, end: Instant? = null)

    fun updateUserSessionKey(username: Username, sessionKey: BigUnsignedInteger)
    fun getUserSessionKey(username: Username): BigUnsignedInteger?

    fun recordUserAuthFailure(username: Username): Long

    // TODO: somewhere else?
    fun suspendIp(ip: InetAddress, end: Instant?)
    fun getIpSuspension(ip: InetAddress): IpSuspension?

}

data class IpSuspension(val end: Instant?)
