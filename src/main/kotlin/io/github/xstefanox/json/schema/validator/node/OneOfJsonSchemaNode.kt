package io.github.xstefanox.json.schema.validator.node

data class OneOfJsonSchemaNode(val oneOf: List<JsonSchemaNode>) : JsonSchemaNode() {

    init {
        if (oneOf.isEmpty()) {
            throw IllegalArgumentException("the list of nested JSON Schema must not be empty")
        }
    }
}