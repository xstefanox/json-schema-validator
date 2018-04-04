package io.github.xstefanox.json.schema.validator.node

data class AnyOfJsonSchemaNode(val anyOf: List<JsonSchemaNode>) : JsonSchemaNode() {

    init {
        if (anyOf.isEmpty()) {
            throw IllegalArgumentException("the list of nested JSON Schema must not be empty")
        }
    }
}