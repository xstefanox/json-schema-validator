package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
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

                val intValue = json.asInt()

                if (root.multipleOf != null && !root.multipleOf.divides(intValue)) {
                    errors.add("$intValue is not a multiple of ${root.multipleOf}")
                }

                if (root.minimum != null) {
                    if (root.exclusiveMinimum && intValue <= root.minimum) {
                        errors.add("$intValue is lesser than lower bound")
                    } else if (intValue < root.minimum) {
                        errors.add("$intValue is lesser than lower bound")
                    }
                }

                if (root.maximum != null) {
                    if (root.exclusiveMaximum && intValue >= root.maximum) {
                        errors.add("$intValue is greater than upper bound")
                    } else if (intValue > root.maximum) {
                        errors.add("$intValue is greater than upper bound")
                    }
                }
            }

            is NumberJsonSchemaNode -> {

                if (json !is DoubleNode && json !is FloatNode && json !is DecimalNode) {
                    errors.add("expected a number, found ${json::class}")
                } else {

                    val doubleValue = json.asDouble()

                    if (root.multipleOf != null && !root.multipleOf.divides(doubleValue)) {
                        errors.add("$doubleValue is not a multiple of ${root.multipleOf}")
                    }

                    if (root.minimum != null) {
                        if (root.exclusiveMinimum && doubleValue <= root.minimum.toDouble()) {
                            errors.add("$doubleValue is lesser than lower bound")
                        } else if (doubleValue < root.minimum.toDouble()) {
                            errors.add("$doubleValue is lesser than lower bound")
                        }
                    }

                    if (root.maximum != null) {
                        if (root.exclusiveMaximum && doubleValue >= root.maximum.toDouble()) {
                            errors.add("$doubleValue is greater than upper bound")
                        } else if (doubleValue > root.maximum.toDouble()) {
                            errors.add("$doubleValue is greater than upper bound")
                        }
                    }
                }
            }

            is NullJsonSchemaNode -> {
                if (json !is NullNode) {
                    errors.add("expected a null, found ${json::class}")
                }
            }

            is StringJsonSchemaNode -> {

                if (json !is TextNode) {
                    errors.add("expected a string, found ${json::class}")
                }

                val textValue = json.asText()

                if (root.minLength > textValue.length) {
                    errors.add("minimum length is ${root.minLength}, found ${textValue.length}")
                }

                if (root.maxLength != null && root.maxLength < textValue.length) {
                    errors.add("maximum length is ${root.maxLength}, found ${textValue.length}")
                }

                if (root.pattern != null && !root.pattern.matches(textValue)) {
                    errors.add("string  does not match pattern")
                }
            }

            else -> throw AssertionError("unsupported JSON Schema type")
        }

        return ValidationResult(errors)
    }
}