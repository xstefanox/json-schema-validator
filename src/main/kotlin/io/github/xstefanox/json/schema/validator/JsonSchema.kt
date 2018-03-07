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


private val ROOT_POINTER = JsonPointer.compile("/")


private fun JsonPointer.append(tail: String) : JsonPointer {
    return append(JsonPointer.valueOf("/$tail"))
}


private fun JsonPointer.append(tail: Int) : JsonPointer {
    return append(tail.toString())
}


private fun JsonPointer.append(tail: ObjectJsonSchemaNode.Property) : JsonPointer {
    return append(tail.toString())
}


class JsonSchema(private val root: JsonSchemaNode, val schema: URI) {

    companion object {

        @JvmField
        val SCHEMA_POINTER: JsonPointer = JsonPointer.compile("/\$schema")

        const val SCHEMA_FIELD = "\$schema"

        @JvmField
        val DEFAULT_SCHEMA_URI = URI.create("http://json-schema.org/schema#")!!
    }

    fun validate(json: JsonNode): ValidationResult {
        return ValidationResult(validate(root, json, ROOT_POINTER))
    }

    private fun validate(schema: BooleanJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json !is BooleanNode) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be a boolean"
                    )
            )
        }

        return emptyList()
    }

    private fun validate(schema: IntegerJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json::class !in INTEGER_NODE_CLASSES) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be an integer"
                    )
            )
        }

        val errors = mutableListOf<ValidationError>()

        val longValue = json.asLong()

        if (schema.multipleOf != null && !schema.multipleOf.divides(longValue)) {
            errors += ValidationError(
                    pointer,
                    "element should be a multiple of ${schema.multipleOf}"
            )
        }

        if (schema.minimum != null) {
            if (schema.exclusiveMinimum && longValue <= schema.minimum) {
                errors += ValidationError(
                        pointer,
                        "element should be greater than ${schema.minimum}"
                )
            } else if (longValue < schema.minimum) {
                errors += ValidationError(
                        pointer,
                        "element should be greater than or equal to ${schema.minimum}"
                )
            }
        }

        if (schema.maximum != null) {
            if (schema.exclusiveMaximum && longValue >= schema.maximum) {
                errors += ValidationError(
                        pointer,
                        "element should be less than ${schema.maximum}"
                )
            } else if (longValue > schema.maximum) {
                errors += ValidationError(
                        pointer,
                        "element should be less than or equal to ${schema.maximum}"
                )
            }
        }

        return errors
    }

    private fun validate(schema: NumberJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json::class !in FLOAT_NODE_CLASSES) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be a number"
                    )
            )
        }

        val errors = mutableListOf<ValidationError>()

        val doubleValue = json.asDouble()

        if (schema.multipleOf != null && !schema.multipleOf.divides(doubleValue)) {
            errors += ValidationError(
                    pointer,
                    "element should be a multiple of ${schema.multipleOf}"
            )
        }

        if (schema.minimum != null) {
            if (schema.exclusiveMinimum && doubleValue <= schema.minimum.toDouble()) {
                errors += ValidationError(
                        pointer,
                        "element should be greater than ${schema.minimum}"
                )
            } else if (doubleValue < schema.minimum.toDouble()) {
                errors += ValidationError(
                        pointer,
                        "element should be greater than or equal to ${schema.minimum}"
                )
            }
        }

        if (schema.maximum != null) {
            if (schema.exclusiveMaximum && doubleValue >= schema.maximum.toDouble()) {
                errors += ValidationError(
                        pointer,
                        "element should be less than ${schema.maximum}"
                )
            } else if (doubleValue > schema.maximum.toDouble()) {
                errors += ValidationError(
                        pointer,
                        "element should be less than or equal to ${schema.maximum}"
                )
            }
        }

        return errors
    }

    private fun validate(schema: NullJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json !is NullNode) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be null"
                    )
            )
        }

        return emptyList()
    }

    private fun validate(schema: StringJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json !is TextNode) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be a string"
                    )
            )
        }

        val errors = mutableListOf<ValidationError>()

        val textValue = json.asText()

        if (schema.minLength > textValue.length) {
            errors +=
                    ValidationError(
                            pointer,
                            "string should be at least ${schema.minLength} characters long"
                    )
        }

        if (schema.maxLength != null && schema.maxLength < textValue.length) {
            errors += ValidationError(
                    pointer,
                    "string should be at most ${schema.maxLength} characters long"
            )
        }

        if (schema.pattern != null && !schema.pattern.matches(textValue)) {
            errors += ValidationError(
                    pointer,
                    "element does not match pattern"
            )
        }

        return errors
    }

    private fun validate(schema: ArrayJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json !is ArrayNode) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be an array"
                    )
            )
        }

        val errors = mutableListOf<ValidationError>()

        if (schema.minItems > json.size()) {
            errors += ValidationError(
                    pointer,
                    "expected at least ${schema.minItems} items"
            )
        }

        if (schema.maxItems != null && schema.maxItems < json.size()) {
            errors += ValidationError(
                    pointer,
                    "expected at most ${schema.maxItems} items"
            )
        }

        if (schema.uniqueItems) {

            val uniqueItems = mutableSetOf<JsonNode>()

            json.forEachIndexed { index, jsonNode ->

                val added = uniqueItems.add(jsonNode)

                if (!added) {
                    errors += ValidationError(
                            pointer.append(index),
                            "duplicated element"
                    )
                }
            }
        }

        if (schema.items != null) {

            json.forEachIndexed { index, jsonNode ->
                errors += validate(schema.items, jsonNode, pointer.append(index))
            }
        }

        return errors
    }

    private fun validate(schema: ObjectJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json !is ObjectNode) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be an object"
                    )
            )
        }

        val errors = mutableListOf<ValidationError>()

        val jsonPropertyNames = json.fieldNames()
                .asSequence()
                .map { ObjectJsonSchemaNode.Property(it) }
                .toSet()

        if (schema.properties != null) {

            for (field in json.fields()) {

                val property = ObjectJsonSchemaNode.Property(field.key)
                val propertyJsonSchemaNode = schema.properties[property]

                if (propertyJsonSchemaNode != null) {
                    errors += validate(propertyJsonSchemaNode, field.value, pointer.append(field.key))
                }
            }

            if (!schema.additionalProperties) {

                jsonPropertyNames
                        .filter { it !in schema.properties.keys }
                        .forEach { property ->
                            errors += ValidationError(
                                    pointer.append(property),
                                    "additional properties not allowed")
                        }
            }
        }

        if (schema.minProperties > jsonPropertyNames.size) {
            errors += ValidationError(
                    pointer,
                    "element should have at least ${schema.minProperties} properties"
            )
        }

        if (schema.maxProperties != null && schema.maxProperties < jsonPropertyNames.size) {
            errors += ValidationError(
                    pointer,
                    "element should have at most ${schema.maxProperties} properties"
            )
        }

        schema.required
                .filter { it !in jsonPropertyNames }
                .forEach { property ->
                    errors += ValidationError(
                            pointer.append(property),
                            "property is missing"
                    )
                }

        return errors
    }

    private fun validate(schema: JsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        return when (schema) {
            is BooleanJsonSchemaNode -> validate(schema, json, pointer)
            is IntegerJsonSchemaNode -> validate(schema, json, pointer)
            is NumberJsonSchemaNode -> validate(schema, json, pointer)
            is NullJsonSchemaNode -> validate(schema, json, pointer)
            is StringJsonSchemaNode -> validate(schema, json, pointer)
            is ArrayJsonSchemaNode -> validate(schema, json, pointer)
            is ObjectJsonSchemaNode -> validate(schema, json, pointer)
            else -> throw AssertionError("unsupported JSON Schema type ${schema::class}")
        }
    }
}
