package io.github.xstefanox.json.schema.validator.node

import io.github.xstefanox.json.schema.validator.model.PositiveInt

data class ArrayJsonSchemaNode(
        val items: JsonSchemaNode? = null,
        val minItems: PositiveInt = PositiveInt(0),
        val maxItems: PositiveInt? = null,
        val uniqueItems: Boolean = false
) : JsonSchemaNode() {

    init {
        if (maxItems != null && maxItems < minItems) {
            throw IllegalArgumentException("maxItems must be greater than or equal to minItems")
        }
    }
}
