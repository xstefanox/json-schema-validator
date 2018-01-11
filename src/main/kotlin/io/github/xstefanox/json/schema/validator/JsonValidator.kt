package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.databind.JsonNode

class JsonValidator {

    private val trueJsonSchema = BooleanJsonSchema(true)
    private val falseJsonSchema = BooleanJsonSchema(false)

    fun validate(jsonSchema: JsonSchema, jsonNode: JsonNode): Boolean {

        if (jsonSchema == trueJsonSchema) {
            return true
        }

        if (jsonSchema == falseJsonSchema) {
            return false
        }

        return false
    }
}