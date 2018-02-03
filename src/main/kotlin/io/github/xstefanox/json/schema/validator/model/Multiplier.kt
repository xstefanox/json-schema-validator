package io.github.xstefanox.json.schema.validator.model

import com.fasterxml.jackson.annotation.JsonValue

data class Multiplier(private val value: Number) : Comparable<Multiplier> {

    init {

        if (value.toDouble() <= 0) {
            throw IllegalArgumentException("multiplier must be greater than 0")
        }
    }

    override fun compareTo(other: Multiplier): Int {
        return value.toDouble().compareTo(other.value.toDouble())
    }

    @JsonValue
    fun toJson(): Number {
        return value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) {
            return true
        }

        if (javaClass != other?.javaClass) {
            return false
        }

        other as Multiplier

        if (value.toDouble() != other.value.toDouble()) {
            return false
        }

        return true
    }

    override fun toString(): String {
        return value.toString()
    }
}