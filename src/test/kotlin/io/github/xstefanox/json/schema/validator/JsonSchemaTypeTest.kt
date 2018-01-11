package io.github.xstefanox.json.schema.validator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class JsonSchemaTypeTest {

    companion object {
        const val SCHEMA_TYPE_COUNT = 6
    }

    @Test
    @DisplayName("JSON Schema type should be $SCHEMA_TYPE_COUNT")
    internal fun test1() {
        assertThat(JsonSchemaType.values().size).isEqualTo(SCHEMA_TYPE_COUNT)
    }

    @Test
    @DisplayName("JsonSchemaType should be deserialized from JSON schema type name")
    internal fun test2() {
        assertThat(JsonSchemaType.fromJsonSchemaString("boolean")).isEqualTo(JsonSchemaType.BOOLEAN)
    }

    @Test
    @DisplayName("JsonSchemaType deserialization should be case sensitive")
    internal fun test3() {
        assertThrows(IllegalArgumentException::class.java, {
            JsonSchemaType.fromJsonSchemaString("BOOLEAN")
        })
    }
}