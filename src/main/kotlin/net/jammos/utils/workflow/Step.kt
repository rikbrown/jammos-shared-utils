package net.jammos.utils.workflow

import kotlin.reflect.KClass

abstract class Step<T : Any, out R: Any>(val messageType: KClass<T>) {

    fun handle(msg: Any): ResponseAndNextStep<R> {
        return when {
            messageType.isInstance(msg) -> handle0(messageType.java.cast(msg))
            else -> throw RuntimeException("Unexpected message type: ${msg::class.qualifiedName}")
        }
    }

    abstract fun handle0(msg: T): ResponseAndNextStep<R>

    data class ResponseAndNextStep<out R: Any>(
            val response: R,
            val nextStep: Step<*, *>? = null)
}


