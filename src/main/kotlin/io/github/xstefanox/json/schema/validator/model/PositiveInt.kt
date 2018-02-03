package io.github.xstefanox.json.schema.validator.model

import com.fasterxml.jackson.annotation.JsonValue

data class PositiveInt(private val value: Int) : Comparable<PositiveInt> {

    init {
        if (value < 0) {
            throw IllegalArgumentException("value must be greater or equal to 0")
        }
    }

    override fun compareTo(other: PositiveInt): Int {
        return value.compareTo(other.value)
    }

    operator fun compareTo(other: Int): Int {
        return value.compareTo(other)
    }

    override fun toString(): String {
        return value.toString()
    }

    @JsonValue
    fun toJson() : Int {
        return value
    }
}
