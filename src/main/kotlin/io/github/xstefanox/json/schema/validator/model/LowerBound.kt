package io.github.xstefanox.json.schema.validator.model

data class LowerBound(val value: Number, val exclusive: Boolean = false) {

    fun accepts(number: Number): Boolean {

        return if (exclusive) {
            number.toDouble() > value.toDouble()
        } else {
            number.toDouble() >= value.toDouble()
        }
    }
}
