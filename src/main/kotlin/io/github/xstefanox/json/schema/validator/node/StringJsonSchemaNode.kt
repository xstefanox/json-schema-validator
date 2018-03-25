package io.github.xstefanox.json.schema.validator.node

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonAppend
import io.github.xstefanox.json.schema.validator.TypePropertyWriter
import io.github.xstefanox.json.schema.validator.model.ESRegex
import io.github.xstefanox.json.schema.validator.model.PositiveInt

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAppend(
        prepend = true,
        props = [
            JsonAppend.Prop(name = "type", value = TypePropertyWriter::class)
        ]
)
data class StringJsonSchemaNode(
        val minLength: PositiveInt = PositiveInt(0),
        val maxLength: PositiveInt? = null,
        val pattern: ESRegex? = null,
        val format: StringFormat? = null
) : JsonSchemaNode() {

    init {

        if (maxLength != null && minLength > maxLength) {
            throw IllegalArgumentException("maxLength must be geater or equal to minLength")
        }
    }
}
