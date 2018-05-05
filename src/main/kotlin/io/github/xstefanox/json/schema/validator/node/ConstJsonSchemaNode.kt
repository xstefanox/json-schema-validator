package io.github.xstefanox.json.schema.validator.node

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.node.ObjectNode

abstract class ConstJsonSchemaNode : JsonSchemaNode()

data class StringConstJsonSchemaNode(val const: String) : ConstJsonSchemaNode()

data class IntegerConstJsonSchemaNode(val const: Int) : ConstJsonSchemaNode()

data class NumberConstJsonSchemaNode(val const: Number) : ConstJsonSchemaNode()

data class ArrayConstJsonSchemaNode(val const: List<Any>) : ConstJsonSchemaNode()

data class BooleanConstJsonSchemaNode(val const: Boolean) : ConstJsonSchemaNode()

data class ObjectConstJsonSchemaNode(val const: ObjectNode) : ConstJsonSchemaNode()

object NullConstJsonSchemaNode : ConstJsonSchemaNode() {

    @JsonValue
    fun toJson(): Map<String, Nothing?> {
        return mapOf("const" to null)
    }
}
