package net.jammos.utils.types

abstract class ReversibleEnum<out T: Any, in V>(values: Array<T>, reverser: (T) -> V) {
    private val byValue = values.associateBy { reverser(it) }

    fun ofValue(value: V) = ofValueOrNull(value) ?: throw IllegalArgumentException("Unknown value: " + value)
    fun ofValueOrNull(value: V) = byValue[value]
}

abstract class ReversibleByte<out T: WriteableByte>(values: Array<T>): ReversibleEnum<T, Short>(values, WriteableByte::value)
