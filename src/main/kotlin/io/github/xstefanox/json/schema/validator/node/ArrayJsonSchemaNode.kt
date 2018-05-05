package io.github.xstefanox.json.schema.validator.node

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonAppend
import io.github.xstefanox.json.schema.validator.TypePropertyWriter
import io.github.xstefanox.json.schema.validator.model.PositiveInt

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAppend(
    prepend = true,
    props = [
        JsonAppend.Prop(name = "type", value = TypePropertyWriter::class)
    ]
)
data class ArrayJsonSchemaNode(
    val items: JsonSchemaNode? = null,
    val minItems: PositiveInt = PositiveInt(0),
    val maxItems: PositiveInt? = null,
    val uniqueItems: Boolean = false,
    val contains: JsonSchemaNode? = null
) : JsonSchemaNode() {

    init {
        if (maxItems != null && maxItems < minItems) {
            throw IllegalArgumentException("maxItems must be greater than or equal to minItems")
        }
    }
}
