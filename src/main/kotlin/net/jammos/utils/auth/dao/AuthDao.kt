package net.jammos.utils.auth.dao

import net.jammos.utils.auth.UserAuth
import net.jammos.utils.auth.UserId
import net.jammos.utils.auth.UserSuspension
import net.jammos.utils.auth.Username
import net.jammos.utils.types.BigUnsignedInteger
import java.net.InetAddress
import java.time.Instant

interface AuthDao {
    fun getUserAuth(username: Username): UserAuth?
    fun getUserSuspension(userId: UserId): UserSuspension?

    fun createUser(username: Username, password: String): UserAuth

    fun suspendUser(userId: UserId, start: Instant, end: Instant? = null)

    fun updateUserSessionKey(userId: UserId, sessionKey: BigUnsignedInteger)
    fun getUserSessionKey(userId: UserId): BigUnsignedInteger?

    fun recordUserAuthFailure(userId: UserId): Long

    // TODO: somewhere else?
    fun suspendIp(ip: InetAddress, end: Instant?)
    fun getIpSuspension(ip: InetAddress): IpSuspension?

}

data class IpSuspension(val end: Instant?)
