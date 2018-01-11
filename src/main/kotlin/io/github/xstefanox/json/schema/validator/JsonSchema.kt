package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import java.net.URI

class JsonSchema(json: JsonNode) {

    companion object {

        @JvmField
        val SCHEMA_POINTER: JsonPointer = JsonPointer.compile("/\$schema")

        @JvmField
        val DESCRIPTION_POINTER: JsonPointer = JsonPointer.compile("/description")

        @JvmField
        val TYPE_POINTER: JsonPointer = JsonPointer.compile("/type")
    }

    val schema: URI
    val description: String?
    val type: JsonSchemaType

    init {

        if (json !is ObjectNode && json !is BooleanNode) {
            throw IllegalArgumentException("a JSON Schema must be an object or a boolean")
        }

        this.schema = URI.create((json.at(SCHEMA_POINTER) as? TextNode ?: throw IllegalArgumentException("\$schema must be a string")).asText())
        this.description = (json.at(DESCRIPTION_POINTER) as? TextNode)?.asText()
        this.type = JsonSchemaType.fromJsonSchemaString((json.at(TYPE_POINTER) as? TextNode ?: throw IllegalArgumentException("type must be a string")).asText())
    }
}