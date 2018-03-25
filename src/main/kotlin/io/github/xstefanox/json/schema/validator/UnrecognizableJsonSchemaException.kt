package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.databind.JsonNode

/**
 * Thrown when no JSON Schema cannot be deserialized from a JSON string.
 */
class UnrecognizableJsonSchemaException(json: JsonNode) : RuntimeException("unrecongizable JSON Schema: $json")
