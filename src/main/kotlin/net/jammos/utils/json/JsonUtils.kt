package net.jammos.utils.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlin.reflect.KClass

val objectMapper: ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .registerModule(bigUnsignedIntegerModule)

inline fun <reified T: Any> String.fromJson(): T = objectMapper.readValue(this)
inline fun <reified T: Any> String.fromJson(clazz: KClass<T>): T = objectMapper.readValue(this, clazz.java)
fun Any.toJson(): String = objectMapper.writeValueAsString(this)
