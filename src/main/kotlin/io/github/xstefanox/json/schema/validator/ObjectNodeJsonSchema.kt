package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import java.net.URI

class ObjectNodeJsonSchema(json: ObjectNode) : JsonSchema() {

    companion object {

        @JvmField
        val SCHEMA_POINTER: JsonPointer = JsonPointer.compile("/\$schema")

        @JvmField
        val DESCRIPTION_POINTER: JsonPointer = JsonPointer.compile("/description")

        @JvmField
        val TYPE_POINTER: JsonPointer = JsonPointer.compile("/type")

        @JvmField
        val DEFAULT_SCHEMA_URI = URI.create("http://json-schema.org/schema#")!!
    }

    val schema: URI
    val description: String?
    val type: JsonSchemaType?

    init {

        val schema = json.at(SCHEMA_POINTER)

        if (schema is TextNode) {
            this.schema = URI.create(schema.asText())
        } else {
            this.schema = DEFAULT_SCHEMA_URI
        }

        this.description = (json.at(DESCRIPTION_POINTER) as? TextNode)?.asText()

        val type = json.at(TYPE_POINTER)

        if (type is TextNode) {
            this.type = JsonSchemaType.fromJsonSchemaString(type.asText())
        } else {
            this.type = null
        }
    }
}