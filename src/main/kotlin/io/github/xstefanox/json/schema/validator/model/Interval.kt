package io.github.xstefanox.json.schema.validator.model

class Interval(val lowerBound: LowerBound?, val upperBound: UpperBound?) {

    constructor(lowerBound: LowerBound) : this(lowerBound, null)
    constructor(upperBound: UpperBound) : this(null, upperBound)

    init {
    }

    operator fun contains(number: Number): Boolean {
        return lowerBound?.accepts(number) ?: true && upperBound?.accepts(number) ?: true
    }
}