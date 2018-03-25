package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.convertValue
import io.github.xstefanox.json.schema.validator.node.ArrayConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.ArrayJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.BooleanConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.BooleanJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.IntegerConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.IntegerJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.JsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NullConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NullJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NumberConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NumberJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.ObjectConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.ObjectJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.StringConstJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.StringJsonSchemaNode


private const val CONST_FIELD_NAME = "const"

private const val TYPE_FIELD_NAME = "type"


class JsonSchemaNodeDeserializer : StdDeserializer<JsonSchemaNode>(JsonSchemaNode::class.java) {

    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext?): JsonSchemaNode {

        val objectMapper = jsonParser.codec as ObjectMapper
        val json = jsonParser.readValueAsTree<JsonNode>()

        if (json is ObjectNode) {

            if (json.has(CONST_FIELD_NAME)) {

                val const = json.get(CONST_FIELD_NAME)

                if (const::class in INTEGER_NODE_CLASSES) {
                    return objectMapper.convertValue<IntegerConstJsonSchemaNode>(json)
                }

                if (const::class in NUMBER_NODE_CLASSES) {
                    return objectMapper.convertValue<NumberConstJsonSchemaNode>(json)
                }

                if (const is TextNode) {
                    return objectMapper.convertValue<StringConstJsonSchemaNode>(json)
                }

                if (const is NullNode) {
                    return NullConstJsonSchemaNode
                }

                if (const is BooleanNode) {
                    return objectMapper.convertValue<BooleanConstJsonSchemaNode>(json)
                }

                if (const is ArrayNode) {
                    return objectMapper.convertValue<ArrayConstJsonSchemaNode>(json)
                }

                if (const is ObjectNode) {
                    return objectMapper.convertValue<ObjectConstJsonSchemaNode>(json)
                }
            }

            if (json.has(TYPE_FIELD_NAME)) {

                val type = json.get(TYPE_FIELD_NAME).asText()

                return when (type) {
                    "integer" -> objectMapper.convertValue<IntegerJsonSchemaNode>(json)
                    "number" -> objectMapper.convertValue<NumberJsonSchemaNode>(json)
                    "string" -> objectMapper.convertValue<StringJsonSchemaNode>(json)
                    "boolean" -> objectMapper.convertValue<BooleanJsonSchemaNode>(json)
                    "null" -> objectMapper.convertValue<NullJsonSchemaNode>(json)
                    "object" -> objectMapper.convertValue<ObjectJsonSchemaNode>(json)
                    "array" -> objectMapper.convertValue<ArrayJsonSchemaNode>(json)
                    else -> throw UnsupportedJsonSchemaTypeException(type)
                }
            }
        }

        throw UnrecognizableJsonSchemaException(json)
    }
}
