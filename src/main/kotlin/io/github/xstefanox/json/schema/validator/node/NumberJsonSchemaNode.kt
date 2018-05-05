package io.github.xstefanox.json.schema.validator.node

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonAppend
import io.github.xstefanox.json.schema.validator.TypePropertyWriter
import io.github.xstefanox.json.schema.validator.model.Multiplier

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAppend(
    prepend = true,
    props = [
        JsonAppend.Prop(name = "type", value = TypePropertyWriter::class)
    ]
)
data class NumberJsonSchemaNode(
    val multipleOf: Multiplier? = null,
    val minimum: Number? = null,
    val maximum: Number? = null,
    val exclusiveMinimum: Number? = null,
    val exclusiveMaximum: Number? = null
) : JsonSchemaNode() {

    init {

        if (minimum != null && maximum != null && maximum.toDouble() < minimum.toDouble()) {
            throw IllegalArgumentException("maximum must be greater or equal to minimum")
        }
    }
}
