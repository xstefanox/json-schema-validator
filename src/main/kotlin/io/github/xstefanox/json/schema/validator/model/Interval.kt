package io.github.xstefanox.json.schema.validator.model

data class Interval(val lowerBound: LowerBound?, val upperBound: UpperBound?) {

    constructor(lowerBound: LowerBound) : this(lowerBound, null)
    constructor(upperBound: UpperBound) : this(null, upperBound)

    init {
        if (lowerBound != null && upperBound != null && upperBound < lowerBound) {
            throw IllegalArgumentException("upper bound cannot be lesser than lower bound")
        }
    }

    operator fun contains(number: Number): Boolean {
        return lowerBound?.accepts(number) ?: true && upperBound?.accepts(number) ?: true
    }
}