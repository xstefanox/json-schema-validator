package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AnyOfJsonSchemaNodeTest {

    @Test
    @DisplayName("anyOf JSON Schema node can be serialized")
    internal fun test1() {

        val jsonSchema = AnyOfJsonSchemaNode(listOf(
                BooleanJsonSchemaNode,
                IntegerJsonSchemaNode()
        ))

        assertThat(OBJECT_MAPPER.writeValueAsString(jsonSchema)).isEqualTo("""
            {
              "anyOf" : [ {
                "type" : "boolean"
              }, {
                "type" : "integer",
                "multipleOf" : null,
                "minimum" : null,
                "maximum" : null,
                "exclusiveMinimum" : null,
                "exclusiveMaximum" : null
              } ]
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("anyOf JSON Schema node can be deserialized")
    internal fun test2() {

        val jsonSchema = AnyOfJsonSchemaNode(listOf(
                NullJsonSchemaNode,
                NumberJsonSchemaNode()
        ))

        assertThat(OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
              "anyOf" : [
                {
                  "type" : "null"
                },
                {
                  "type" : "number"
                }
              ]
            }
            """)).isEqualTo(jsonSchema)
    }

    @Test
    @DisplayName("the list of nested JSON Schema must not be empty")
    internal fun test3() {
        assertThrows<IllegalArgumentException> {
            AnyOfJsonSchemaNode(emptyList())
        }
    }
}