package io.github.xstefanox.json.schema.validator

enum class JsonSchemaType {

    NULL, BOOLEAN, OBJECT, ARRAY, NUMBER, STRING;

    companion object {

        private val lowercaseValues = values().map { it.name.toLowerCase() }

        fun fromJsonSchemaString(value: String) :JsonSchemaType {

            val lowercaseName = lowercaseValues.find { it == value } ?: throw IllegalArgumentException("invalid value " + value)

            return valueOf(lowercaseName.toUpperCase())
        }
    }
}