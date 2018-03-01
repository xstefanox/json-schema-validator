package io.github.xstefanox.json.schema.validator.model

data class LowerBound(val value: Number, val exclusive: Boolean = false) : Comparable<LowerBound> {

    fun accepts(number: Number): Boolean {

        return if (exclusive) {
            number.toDouble() > value.toDouble()
        } else {
            number.toDouble() >= value.toDouble()
        }
    }

    override operator fun compareTo(other: LowerBound): Int {
        return value.toDouble().compareTo(other.value.toDouble())
    }

    operator fun compareTo(other: UpperBound): Int {
        return value.toDouble().compareTo(other.value.toDouble())
    }
}