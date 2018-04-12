package io.github.xstefanox.json.schema.validator.node

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonAppend
import io.github.xstefanox.json.schema.validator.TypePropertyWriter

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAppend(
        prepend = true,
        props = [
            JsonAppend.Prop(name = "type", value = TypePropertyWriter::class)
        ]
)
object NullJsonSchemaNode : JsonSchemaNode()
