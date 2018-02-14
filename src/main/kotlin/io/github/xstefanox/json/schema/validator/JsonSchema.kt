package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.BigIntegerNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.LongNode
import com.fasterxml.jackson.databind.node.ShortNode
import io.github.xstefanox.json.schema.validator.node.BooleanJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.IntegerJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.JsonSchemaNode
import java.net.URI

class JsonSchema(private val root: JsonSchemaNode, val schema: URI) {

    companion object {

        @JvmField
        val SCHEMA_POINTER: JsonPointer = JsonPointer.compile("/\$schema")

        const val SCHEMA_FIELD = "\$schema"

        @JvmField
        val DEFAULT_SCHEMA_URI = URI.create("http://json-schema.org/schema#")!!
    }

    fun validate(json: JsonNode): ValidationResult {

        val errors = mutableListOf<String>()

        when (root) {
            is BooleanJsonSchemaNode -> {
                if (json !is BooleanNode) {
                    errors.add("expected a ${BooleanNode::class}, found ${json::class}")
                }
            }

            is IntegerJsonSchemaNode -> {
                if (json !is IntNode && json !is BigIntegerNode && json !is ShortNode && json !is LongNode) {
                    errors.add("expected an integer, found ${json::class}")
                }
            }
        }

        return ValidationResult(errors)
    }
}