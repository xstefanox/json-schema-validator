package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
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
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import inet.ipaddr.HostName
import io.github.xstefanox.json.schema.validator.node.AlwaysValidatingJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.ArrayConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.ArrayJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.BooleanConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.BooleanJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.EnumJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.IntegerConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.IntegerJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.JsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NeverValidatingJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NotJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NullConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NullJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NumberConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NumberJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.ObjectConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.ObjectJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.OneOfJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.StringConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.StringFormat.DATE
import io.github.xstefanox.json.schema.validator.node.StringFormat.DATE_TIME
import io.github.xstefanox.json.schema.validator.node.StringFormat.EMAIL
import io.github.xstefanox.json.schema.validator.node.StringFormat.HOSTNAME
import io.github.xstefanox.json.schema.validator.node.StringFormat.TIME
import io.github.xstefanox.json.schema.validator.node.StringJsonSchemaNode
import java.net.URI
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.time.format.DateTimeFormatter.ISO_OFFSET_TIME
import java.time.format.DateTimeParseException
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress


/**
 * The [Set] of [com.fasterxml.jackson.databind.node.NumericNode]s that can contain integer values.
 */
val INTEGER_NODE_CLASSES = setOf(
        IntNode::class,
        BigIntegerNode::class,
        ShortNode::class,
        LongNode::class
)


/**
 * The [Set] of [com.fasterxml.jackson.databind.node.NumericNode]s that can contain floating point values.
 */
val NUMBER_NODE_CLASSES = setOf(
        DoubleNode::class,
        FloatNode::class,
        DecimalNode::class
)


private val ROOT_POINTER = JsonPointer.compile("/")


private fun JsonPointer.append(tail: String): JsonPointer {
    return append(JsonPointer.valueOf("/$tail"))
}


private fun JsonPointer.append(tail: Int): JsonPointer {
    return append(tail.toString())
}


private fun JsonPointer.append(tail: ObjectJsonSchemaNode.Property): JsonPointer {
    return append(tail.toString())
}


class JsonSchema(private val root: JsonSchemaNode, val schema: URI) {

    private val objectMapper: ObjectMapper by lazy {

        ObjectMapper()
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .registerModule(KotlinModule())
    }

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

        if (json::class !in NUMBER_NODE_CLASSES) {
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

        if (schema.format != null) {
            when (schema.format) {
                DATE -> {
                    try {
                        ISO_OFFSET_DATE.parse(textValue)
                    } catch (e: DateTimeParseException) {
                        errors += ValidationError(
                                pointer,
                                "element should be a date"
                        )
                    }
                }
                TIME -> {
                    try {
                        ISO_OFFSET_TIME.parse(textValue)
                    } catch (e: DateTimeParseException) {
                        errors += ValidationError(
                                pointer,
                                "element should be a time"
                        )
                    }
                }
                DATE_TIME -> {
                    try {
                        ISO_OFFSET_DATE_TIME.parse(textValue)
                    } catch (e: DateTimeParseException) {
                        errors += ValidationError(
                                pointer,
                                "element should be a date-time"
                        )
                    }
                }
                EMAIL -> {
                    try {
                        InternetAddress(textValue)
                    } catch (e: AddressException) {
                        errors += ValidationError(
                                pointer,
                                "element should be an email address"
                        )
                    }
                }
                HOSTNAME -> {
                    if (!HostName(textValue).isValid) {
                        errors += ValidationError(
                                pointer,
                                "element should be a host name"
                        )
                    }
                }
                else -> throw AssertionError("unsupported JSON Schema string format ${schema.format}")
            }
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

        if (schema.contains != null) {

            val validItems = json.filter { jsonNode -> validate(schema.contains, jsonNode, pointer).isEmpty() }

            if (validItems.isEmpty()) {
                return listOf(ValidationError(
                        pointer,
                        """no item matching "contains" schema found"""
                ))
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

    private fun validate(schema: IntegerConstJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json::class !in INTEGER_NODE_CLASSES) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be an integer"
                    )
            )
        }

        if (json.asLong() != schema.const.toLong()) {
            return listOf(ValidationError(
                    pointer,
                    "element should be equal to ${schema.const}"
            ))
        }

        return listOf()
    }

    private fun validate(schema: NumberConstJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json::class !in NUMBER_NODE_CLASSES) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be a number"
                    )
            )
        }

        if (json.asLong() != schema.const.toLong()) {
            return listOf(ValidationError(
                    pointer,
                    "element should be equal to ${schema.const}"
            ))
        }

        return listOf()
    }

    private fun validate(schema: BooleanConstJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json !is BooleanNode) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be a boolean"
                    )
            )
        }

        if (json.asBoolean() != schema.const) {
            return listOf(ValidationError(
                    pointer,
                    "element should be equal to ${schema.const}"
            ))
        }

        return listOf()
    }

    private fun validate(schema: NullConstJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json !is NullNode) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be null"
                    )
            )
        }

        return listOf()
    }

    private fun validate(schema: StringConstJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json !is TextNode) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be a string"
                    )
            )
        }

        if (json.asText() != schema.const) {
            return listOf(ValidationError(
                    pointer,
                    "element should be equal to ${schema.const}"
            ))
        }

        return listOf()
    }

    private fun validate(schema: ArrayConstJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json !is ArrayNode) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be an array"
                    )
            )
        }

        val values = objectMapper.convertValue<List<Any>>(json)

        if (values != schema.const) {
            return listOf(ValidationError(
                    pointer,
                    "element should be equal to ${schema.const}"
            ))
        }

        return listOf()
    }

    private fun validate(schema: ObjectConstJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        if (json !is ObjectNode) {
            return listOf(
                    ValidationError(
                            pointer,
                            "element should be an object"
                    )
            )
        }

        if (json != schema.const) {
            return listOf(ValidationError(
                    pointer,
                    "element should be equal to ${schema.const}"
            ))
        }

        return listOf()
    }

    private fun validate(schema: EnumJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        val values = objectMapper.convertValue<List<JsonNode>>(schema.enum)

        if (json !in values) {
            return listOf(ValidationError(
                    pointer,
                    "element not found in enum"
            ))
        }

        return listOf()
    }

    private fun validate(schema: AlwaysValidatingJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {
        return listOf()
    }

    private fun validate(schema: NeverValidatingJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {
        return listOf(ValidationError(
                pointer,
                "element is not valid"
        ))
    }

    private fun validate(schema: NotJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        val nestedSchemaValidationResult = validate(schema.not, json, pointer)

        return if (nestedSchemaValidationResult.isNotEmpty()) {
            emptyList()
        } else {
            listOf(ValidationError(
                    pointer,
                    "element should not be valid"
            ))
        }
    }

    private fun validate(schema: OneOfJsonSchemaNode, json: JsonNode, pointer: JsonPointer): List<ValidationError> {

        val matchedSchema = schema.oneOf.asSequence().count { schema ->
            validate(schema, json, pointer).isEmpty()
        }

        if (matchedSchema == 0) {
            return listOf(ValidationError(
                    pointer,
                    "element does not match any of the nested schema"
            ))
        }

        if (matchedSchema > 1) {
            return listOf(ValidationError(
                    pointer,
                    "element matches more than one of the nested schema"
            ))
        }

        return emptyList()
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
            is IntegerConstJsonSchemaNode -> validate(schema, json, pointer)
            is BooleanConstJsonSchemaNode -> validate(schema, json, pointer)
            is NullConstJsonSchemaNode -> validate(schema, json, pointer)
            is NumberConstJsonSchemaNode -> validate(schema, json, pointer)
            is StringConstJsonSchemaNode -> validate(schema, json, pointer)
            is ArrayConstJsonSchemaNode -> validate(schema, json, pointer)
            is ObjectConstJsonSchemaNode -> validate(schema, json, pointer)
            is EnumJsonSchemaNode -> validate(schema, json, pointer)
            is AlwaysValidatingJsonSchemaNode -> validate(schema, json, pointer)
            is NeverValidatingJsonSchemaNode -> validate(schema, json, pointer)
            is NotJsonSchemaNode -> validate(schema, json, pointer)
            is OneOfJsonSchemaNode -> validate(schema, json, pointer)
            else -> throw UnsupportedJsonSchemaClassException(schema::class)
        }
    }
}
