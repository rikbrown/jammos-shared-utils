package net.jammos.utils.network.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.AttributeKey
import mu.KLogging

abstract class JammosHandler: ChannelInboundHandlerAdapter() {
    companion object: KLogging()

    override abstract fun channelRead(ctx: ChannelHandlerContext, msg: Any)

    fun pass(ctx: ChannelHandlerContext, msg: Any) {
        logger.debug { "$javaClass passing on: $msg" }
        ctx.fireChannelRead(msg)
    }

    fun finalResponse(ctx: ChannelHandlerContext, msg: Any) {
        respond(ctx, msg)
        closeSession(ctx)
    }

    fun respond(ctx: ChannelHandlerContext, msg: Any) {
        logger.debug { "Responding with: $msg" }
        ctx.writeAndFlush(msg)
    }

    fun closeSession(ctx: ChannelHandlerContext) {
        logger.info { "Closing session" }
        // If we don't specify this, it seems like we close too quickly
        Thread.sleep(500)
        ctx.close()
    }

    @Suppress("OverridingDeprecatedMember") // deprecation warning is only on one base class
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        logger.error(cause) { "Caught exception in handler" }
        handleException(ctx, cause)
    }

    open fun handleException(ctx: ChannelHandlerContext, cause: Throwable) {
        // by default, kick them out
        logger.warn("Disconnecting client because of exception")
        ctx.disconnect()
        ctx.close()
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        logger.info { "Channel inactive" }
    }

    fun <T> attr(ctx: ChannelHandlerContext, key: AttributeKey<T>, value: T): T = ctx.channel().attr(key).getAndSet(value)
    fun <T> attr(ctx: ChannelHandlerContext, key: AttributeKey<T>): T? = ctx.channel().attr(key).get()
}