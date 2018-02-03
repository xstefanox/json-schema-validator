package io.github.xstefanox.json.schema.validator.node

import io.github.xstefanox.json.schema.validator.model.Multiplier

data class NumberJsonSchemaNode(
        val multipleOf: Multiplier? = null,
        val minimum: Number? = null,
        val maximum: Number? = null,
        val exclusiveMinimum: Boolean = false,
        val exclusiveMaximum: Boolean = false
) : JsonSchemaNode() {

    init {

        if (minimum != null && maximum != null && maximum.toDouble() < minimum.toDouble()) {
            throw IllegalArgumentException("maximum must be greater or equal to minimum")
        }
    }
}
