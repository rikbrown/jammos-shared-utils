package net.jammos.utils.auth.dao

import com.google.common.base.Strings
import com.lambdaworks.redis.RedisClient
import mu.KLogging
import net.jammos.utils.ByteArrays.randomBytes
import net.jammos.utils.auth.*
import net.jammos.utils.auth.Username.Username.username
import net.jammos.utils.auth.crypto.CryptoManager
import net.jammos.utils.extensions.minutes
import net.jammos.utils.json.fromJson
import net.jammos.utils.json.toJson
import net.jammos.utils.types.BigUnsignedInteger
import java.net.InetAddress
import java.nio.charset.StandardCharsets.UTF_8
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeParseException

class RedisAuthDao(
        redisClient: RedisClient,
        private val cryptoManager: CryptoManager) : AuthDao {
    private val conn = redisClient.connect().sync()

    private companion object: KLogging() {
        val AUTH_FAILURES_TTL: Duration = 30.minutes

        fun usernameAuthKey(username: Username) = "username:$username:auth"
        fun userUsernameKey(userId: UserId) = "user:$userId:username"
        fun userAuthFailuresKey(userId: UserId) = "user:$userId:auth_failure_count"
        fun userSuspensionKey(userId: UserId) = "user:$userId:suspension"
        fun userSessionKeyKey(userId: UserId) = "user:$userId:session_key"
        fun ipSuspensionKey(ip: InetAddress) = "ip:$ip:suspended_until"
        private val lastUserIdKey = "user:_lastId"
    }

    override fun getUserAuth(username: Username): UserAuth? {
        return conn.get(usernameAuthKey(username))
                ?.fromJson<UserAuth>()
                ?.copy(username = username)
    }

    override fun getUserUsername(userId: UserId) = conn.get(userUsernameKey(userId))?.let { username(it) }

    override fun getUserSuspension(userId: UserId): UserSuspension? {
        return conn.get(userSuspensionKey(userId))?.fromJson()
    }

    override fun createUser(username: Username, password: String): UserAuth {
        // Verify whether the user exists and bail if so
        getUserAuth(username)?.let { throw UserAlreadyExistsException(username) }

        // Create a user ID
        val userId = nextUserId()

        val salt = SaltByteArray(randomBytes(32))
        val passwordUpper = password.toUpperCase()

        val loginHash = cryptoManager.createPrivateKey(
                username.bytes,
                passwordUpper.toByteArray(UTF_8),
                salt)

        val user = UserAuth(userId,
                username = username,
                salt = salt,
                verifier = cryptoManager.createUserVerifier(loginHash))

        // save in redis
        conn.set(usernameAuthKey(username), user.toJson())
        conn.set(userUsernameKey(userId), username.toString())

        return user
    }

    override fun suspendUser(userId: UserId, start: Instant, end: Instant?) {
        conn.set(userSuspensionKey(userId), UserSuspension(
                start = start,
                end = end).toJson())
    }

    override fun updateUserSessionKey(userId: UserId, sessionKey: BigUnsignedInteger) {
        conn.set(userSessionKeyKey(userId), sessionKey.toString())
    }

    override fun getUserSessionKey(userId: UserId): BigUnsignedInteger? {
        return conn.get(userSessionKeyKey(userId))
                ?.let { BigUnsignedInteger.ofHexString(it) }
    }

    override fun recordUserAuthFailure(userId: UserId): Long {
        val key = userAuthFailuresKey(userId)
        return conn.incr(key)
                // expire the auth failures after TTL
                ?.apply { conn.expire(key, AUTH_FAILURES_TTL.seconds) }
                ?: 0
    }

    override fun suspendIp(ip: InetAddress, end: Instant?) {
        val key = ipSuspensionKey(ip)

        // set suspension
        conn.set(key, end?.toString() ?: "")

        // expire suspension key when the suspension is over, if it has an end
        end?.apply { conn.expireat(key, epochSecond) }
    }

    override fun getIpSuspension(ip: InetAddress): IpSuspension? {
        return try {
            conn.get(ipSuspensionKey(ip))
                    // empty string means forever
                    ?.let { IpSuspension(Strings.emptyToNull(it)?.let(Instant::parse)) }

        } catch (e: DateTimeParseException) {
            logger.error(e) { "Failed to parse expiry date" }
            return null
        }
    }

    private fun nextUserId() = UserId(conn.incr(lastUserIdKey).toString())

    class UserAlreadyExistsException(username: Username): IllegalArgumentException("User already exists: $username")
}

/*
    user:<username>:auth = {
        password_sha = <binary>,
        salt: <binary>,
        verifier: <binary>,
    }

    user:<userid>:username = <string>
    user:<userid>:session_key = <binary>
    user:<userid>:auth_failure_count = <int>


    user:<userid>:suspension: {
        start: <time>,
        end: <time>
    }

    io:<ip>:suspended_until: <time>
 */