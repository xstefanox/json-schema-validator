package io.github.xstefanox.json.schema.validator

/**
 * Thrown when an unexpected value of the `type` field is encountered when deserializing a [JsonSchema].
 */
class UnsupportedJsonSchemaTypeException(type: String) : RuntimeException("unsupported JSON Schema type: $type")
