package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class NotJsonSchemaNodeTest {

    @Test
    @DisplayName("not JSON Schema node must be serialized")
    internal fun test1() {
        assertThat(OBJECT_MAPPER.writeValueAsString(NotJsonSchemaNode(BooleanJsonSchemaNode()))).isEqualTo("""
            {
              "not" : {
                "type" : "boolean"
              }
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("not JSON Schema node must be deserialized")
    internal fun test2() {
        assertThat(OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
                "not" : {
                  "type" : "boolean"
                }
            }
            """)).isEqualTo(NotJsonSchemaNode(BooleanJsonSchemaNode()))
    }
}