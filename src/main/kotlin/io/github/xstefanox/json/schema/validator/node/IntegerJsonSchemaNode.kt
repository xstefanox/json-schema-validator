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
data class IntegerJsonSchemaNode(
    val multipleOf: Multiplier? = null,
    val minimum: Int? = null,
    val maximum: Int? = null,
    val exclusiveMinimum: Int? = null,
    val exclusiveMaximum: Int? = null
) : JsonSchemaNode() {

    init {

        if (minimum != null && maximum != null && maximum < minimum) {
            throw IllegalArgumentException("maximum must be greater or equal to minimum")
        }
    }
}
