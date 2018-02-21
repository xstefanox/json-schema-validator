package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BigIntegerNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.DecimalNode
import com.fasterxml.jackson.databind.node.DoubleNode
import com.fasterxml.jackson.databind.node.FloatNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.LongNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ShortNode
import com.fasterxml.jackson.databind.node.TextNode
import io.github.xstefanox.json.schema.validator.node.ArrayJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.BooleanJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.IntegerJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.JsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NullJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NumberJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.StringJsonSchemaNode
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
        return ValidationResult(validate(root, json))
    }

    private fun validate(schema: JsonSchemaNode, json: JsonNode): List<String> {

        val errors = mutableListOf<String>()

        when (schema) {

            is BooleanJsonSchemaNode -> {
                
                if (json !is BooleanNode) {
                    errors += "expected a ${BooleanNode::class}, found ${json::class}"
                }
                
            }

            is IntegerJsonSchemaNode -> {

                if (json !is IntNode && json !is BigIntegerNode && json !is ShortNode && json !is LongNode) {
                    errors += "expected an integer, found ${json::class}"
                }

                val intValue = json.asInt()

                if (schema.multipleOf != null && !schema.multipleOf.divides(intValue)) {
                    errors += "$intValue is not a multiple of ${schema.multipleOf}"
                }

                if (schema.minimum != null) {
                    if (schema.exclusiveMinimum && intValue <= schema.minimum) {
                        errors += "$intValue is lesser than lower bound"
                    } else if (intValue < schema.minimum) {
                        errors += "$intValue is lesser than lower bound"
                    }
                }

                if (schema.maximum != null) {
                    if (schema.exclusiveMaximum && intValue >= schema.maximum) {
                        errors += "$intValue is greater than upper bound"
                    } else if (intValue > schema.maximum) {
                        errors += "$intValue is greater than upper bound"
                    }
                }
            }

            is NumberJsonSchemaNode -> {

                if (json !is DoubleNode && json !is FloatNode && json !is DecimalNode) {
                    errors += "expected a number, found ${json::class}"
                } else {

                    val doubleValue = json.asDouble()

                    if (schema.multipleOf != null && !schema.multipleOf.divides(doubleValue)) {
                        errors += "$doubleValue is not a multiple of ${schema.multipleOf}"
                    }

                    if (schema.minimum != null) {
                        if (schema.exclusiveMinimum && doubleValue <= schema.minimum.toDouble()) {
                            errors += "$doubleValue is lesser than lower bound"
                        } else if (doubleValue < schema.minimum.toDouble()) {
                            errors += "$doubleValue is lesser than lower bound"
                        }
                    }

                    if (schema.maximum != null) {
                        if (schema.exclusiveMaximum && doubleValue >= schema.maximum.toDouble()) {
                            errors += "$doubleValue is greater than upper bound"
                        } else if (doubleValue > schema.maximum.toDouble()) {
                            errors += "$doubleValue is greater than upper bound"
                        }
                    }
                }
            }

            is NullJsonSchemaNode -> {
                if (json !is NullNode) {
                    errors += "expected a null, found ${json::class}"
                }
            }

            is StringJsonSchemaNode -> {

                if (json !is TextNode) {
                    errors += "expected a string, found ${json::class}"
                }

                val textValue = json.asText()

                if (schema.minLength > textValue.length) {
                    errors += "minimum length is ${schema.minLength}, found ${textValue.length}"
                }

                if (schema.maxLength != null && schema.maxLength < textValue.length) {
                    errors += "maximum length is ${schema.maxLength}, found ${textValue.length}"
                }

                if (schema.pattern != null && !schema.pattern.matches(textValue)) {
                    errors += "string  does not match pattern"
                }
            }

            is ArrayJsonSchemaNode -> {

                if (json !is ArrayNode) {
                    errors += "expected an array, found ${json::class}"
                } else {

                    if (schema.minItems > json.size()) {
                        errors += "minimum number of items is ${schema.minItems}, found ${json.size()}"
                    }

                    if (schema.maxItems != null && schema.maxItems < json.size()) {
                        errors += "minimum number of items is ${schema.maxItems}, found ${json.size()}"
                    }

                    if (schema.uniqueItems && json.toList().size != json.toSet().size) {
                        errors += "duplicated elements found"
                    }

                    if (schema.items != null) {
                        for (jsonNode in json) {
                            errors += validate(schema.items, jsonNode)
                        }
                    }
                }
            }

            else -> throw AssertionError("unsupported JSON Schema type ${schema::class}")
        }

        return errors
    }
}