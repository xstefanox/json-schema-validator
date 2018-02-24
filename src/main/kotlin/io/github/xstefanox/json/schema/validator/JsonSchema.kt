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
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.ShortNode
import com.fasterxml.jackson.databind.node.TextNode
import io.github.xstefanox.json.schema.validator.node.ArrayJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.BooleanJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.IntegerJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.JsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NullJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NumberJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.ObjectJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.StringJsonSchemaNode
import java.net.URI


/**
 * The [Set] of [com.fasterxml.jackson.databind.node.NumericNode]s that can contain integer values.
 */
private val INTEGER_NODE_CLASSES = setOf(
        IntNode::class,
        BigIntegerNode::class,
        ShortNode::class,
        LongNode::class
)


/**
 * The [Set] of [com.fasterxml.jackson.databind.node.NumericNode]s that can contain floating point values.
 */
private val FLOAT_NODE_CLASSES = setOf(
        DoubleNode::class,
        FloatNode::class,
        DecimalNode::class
)


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

    private fun validate(schema: BooleanJsonSchemaNode, json: JsonNode): List<String> {

        if (json !is BooleanNode) {
            return listOf("expected a ${BooleanNode::class}, found ${json::class}")
        }

        return emptyList()
    }

    private fun validate(schema: IntegerJsonSchemaNode, json: JsonNode): List<String> {

        if (json::class !in INTEGER_NODE_CLASSES) {
            return listOf("expected an integer, found ${json::class}")
        }

        val errors = mutableListOf<String>()

        val longValue = json.asLong()

        if (schema.multipleOf != null && !schema.multipleOf.divides(longValue)) {
            errors += "$longValue is not a multiple of ${schema.multipleOf}"
        }

        if (schema.minimum != null) {
            if (schema.exclusiveMinimum && longValue <= schema.minimum) {
                errors += "$longValue is lesser than lower bound"
            } else if (longValue < schema.minimum) {
                errors += "$longValue is lesser than lower bound"
            }
        }

        if (schema.maximum != null) {
            if (schema.exclusiveMaximum && longValue >= schema.maximum) {
                errors += "$longValue is greater than upper bound"
            } else if (longValue > schema.maximum) {
                errors += "$longValue is greater than upper bound"
            }
        }

        return errors
    }

    private fun validate(schema: NumberJsonSchemaNode, json: JsonNode): List<String> {

        if (json::class !in FLOAT_NODE_CLASSES) {
            return listOf("expected a number, found ${json::class}")
        }

        val errors = mutableListOf<String>()

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

        return errors
    }

    private fun validate(schema: NullJsonSchemaNode, json: JsonNode): List<String> {

        if (json !is NullNode) {
            return listOf("expected a null, found ${json::class}")
        }

        return emptyList()
    }

    private fun validate(schema: StringJsonSchemaNode, json: JsonNode): List<String> {

        if (json !is TextNode) {
            return listOf("expected a string, found ${json::class}")
        }

        val errors = mutableListOf<String>()

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

        return errors
    }

    private fun validate(schema: ArrayJsonSchemaNode, json: JsonNode): List<String> {

        if (json !is ArrayNode) {
            return listOf("expected an array, found ${json::class}")
        }

        val errors = mutableListOf<String>()

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

        return errors
    }

    private fun validate(schema: ObjectJsonSchemaNode, json: JsonNode): List<String> {

        if (json !is ObjectNode) {
            return listOf("expected an object, found ${json::class}")
        }

        val errors = mutableListOf<String>()

        val jsonPropertyNames = json.fieldNames()
                .asSequence()
                .map { ObjectJsonSchemaNode.Property(it) }
                .toSet()

        if (schema.properties != null) {

            for (field in json.fields()) {

                val property = ObjectJsonSchemaNode.Property(field.key)
                val propertyJsonSchemaNode = schema.properties[property]

                if (propertyJsonSchemaNode != null) {
                    errors += validate(propertyJsonSchemaNode, field.value)
                }
            }

            if (!schema.additionalProperties) {

                jsonPropertyNames
                        .filter { it !in schema.properties.keys }
                        .forEach { errors += "additional property $it not allowed" }
            }
        }

        if (schema.minProperties > jsonPropertyNames.size) {
            errors += "expected at least ${schema.minProperties}, found ${jsonPropertyNames.size}"
        }

        if (schema.maxProperties != null && schema.maxProperties < jsonPropertyNames.size) {
            errors += "expected at most ${schema.maxProperties}, found ${jsonPropertyNames.size}"
        }

        schema.required
                .filter { it !in jsonPropertyNames }
                .forEach { errors += "missing required property $it" }

        return errors
    }

    private fun validate(schema: JsonSchemaNode, json: JsonNode): List<String> {

        return when (schema) {
            is BooleanJsonSchemaNode -> validate(schema, json)
            is IntegerJsonSchemaNode -> validate(schema, json)
            is NumberJsonSchemaNode -> validate(schema, json)
            is NullJsonSchemaNode -> validate(schema, json)
            is StringJsonSchemaNode -> validate(schema, json)
            is ArrayJsonSchemaNode -> validate(schema, json)
            is ObjectJsonSchemaNode -> validate(schema, json)
            else -> throw AssertionError("unsupported JSON Schema type ${schema::class}")
        }
    }
}
