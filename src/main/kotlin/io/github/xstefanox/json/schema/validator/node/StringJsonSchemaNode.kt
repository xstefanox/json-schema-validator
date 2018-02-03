package io.github.xstefanox.json.schema.validator.node

import io.github.xstefanox.json.schema.validator.model.PositiveInt

data class StringJsonSchemaNode(
        val minLength: PositiveInt = PositiveInt(0),
        val maxLength: PositiveInt? = null,
        val pattern: Regex? = null,
        val format: StringFormat? = null
) : JsonSchemaNode() {

    init {

        if (maxLength != null && minLength > maxLength) {
            throw IllegalArgumentException("maxLength must be geater or equal to minLength")
        }
    }
}