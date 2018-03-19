package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class BooleanJsonSchemaNodeTest {

    @Test
    @DisplayName("boolean JSON Schema node must be serialized")
    internal fun test1() {
        assertThat(OBJECT_MAPPER.writeValueAsString(BooleanJsonSchemaNode())).isEqualTo("""
            {
              "type" : "boolean"
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("boolean JSON Schema node must be deserialized")
    internal fun test2() {
        assertThat(OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
              "type" : "boolean"
            }
            """)).isEqualTo(BooleanJsonSchemaNode())
    }

    @Test
    @DisplayName("boolean JSON Schema nodes should always be equal")
    internal fun test3() {
        assertThat(BooleanJsonSchemaNode()).isEqualTo(BooleanJsonSchemaNode())
    }
}