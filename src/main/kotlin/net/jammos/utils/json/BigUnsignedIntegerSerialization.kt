package net.jammos.utils.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import net.jammos.utils.types.BigUnsignedInteger
import java.io.IOException

internal val bigUnsignedIntegerModule = with(SimpleModule()) {
    addSerializer(BigUnsignedInteger::class.java, BigUnsignedIntegerSerializer())
    addDeserializer(BigUnsignedInteger::class.java, BigUnsignedIntegerDeserializer())
}

private class BigUnsignedIntegerSerializer @JvmOverloads constructor(t: Class<BigUnsignedInteger>? = null): StdSerializer<BigUnsignedInteger>(t) {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(
            value: BigUnsignedInteger, jgen: JsonGenerator, provider: SerializerProvider) {

        jgen.writeBinary(value.bytes)
    }
}

private  class BigUnsignedIntegerDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<BigUnsignedInteger>(vc) {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): BigUnsignedInteger {
        val binary = jp.codec.readValue(jp, ByteArray::class.java)
        return BigUnsignedInteger(binary)
    }
}

