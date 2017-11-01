package net.jammos.utils.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val objectMapper: ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .registerModule(bigUnsignedIntegerModule)

inline fun <reified T: Any> String.fromJson(): T = objectMapper.readValue(this)
fun Any.toJson(): String = objectMapper.writeValueAsString(this)
