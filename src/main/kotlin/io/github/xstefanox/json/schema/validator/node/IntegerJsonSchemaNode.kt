package io.github.xstefanox.json.schema.validator.node

import io.github.xstefanox.json.schema.validator.model.Multiplier

data class IntegerJsonSchemaNode(
        val multipleOf: Multiplier? = null,
        val minimum: Int? = null,
        val maximum: Int? = null,
        val exclusiveMinimum: Boolean = false,
        val exclusiveMaximum: Boolean = false
) : JsonSchemaNode() {

    init {

        if (minimum != null && maximum != null && maximum < minimum) {
            throw IllegalArgumentException("maximum must be greater or equal to minimum")
        }
    }
}