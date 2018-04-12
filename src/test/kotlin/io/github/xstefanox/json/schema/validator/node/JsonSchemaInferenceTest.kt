package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.xstefanox.json.schema.validator.UnrecognizableJsonSchemaException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class JsonSchemaInferenceTest {

    @Test
    @DisplayName("known object JSON Schema properties should trigger schema recognition")
    internal fun test1() {

        var jsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
              "properties" : null
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(ObjectJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "required" : [ ]
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(ObjectJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "additionalProperties" : true
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(ObjectJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "minProperties" : 0
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(ObjectJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "maxProperties" : null
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(ObjectJsonSchemaNode::class.java)
    }

    @Test
    @DisplayName("non-object JSON Schema properties should prevent schema recognition")
    internal fun test2() {

        assertThrows<UnrecognizableJsonSchemaException> {

            OBJECT_MAPPER.readValue<JsonSchemaNode>("""
                {
                  "properties" : null,
                  "maxLength" : 5
                }
            """.trimIndent())
        }
    }

    @Test
    @DisplayName("known array JSON Schema properties should trigger schema recognition")
    internal fun test3() {

        var jsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
              "items" : {
                "type": "string"
              }
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(ArrayJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "minItems" : 3
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(ArrayJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "maxItems" : 42
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(ArrayJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "uniqueItems" : true
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(ArrayJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "contains" : {
                "type": "integer"
                }
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(ArrayJsonSchemaNode::class.java)
    }

    @Test
    @DisplayName("non-array JSON Schema properties should prevent schema recognition")
    internal fun test4() {

        assertThrows<UnrecognizableJsonSchemaException> {

            OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
              "items" : {
                "type": "string"
              },
              "maxLength": 2
            }
            """.trimIndent())
        }
    }

    @Test
    @DisplayName("known number JSON Schema properties should trigger schema recognition")
    internal fun test7() {

        var jsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
              "multipleOf" : 3.14
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(NumberJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "minimum" : -1
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(NumberJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "maximum" : 42
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(NumberJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "exclusiveMinimum" : -1
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(NumberJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "exclusiveMaximum" : 42
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(NumberJsonSchemaNode::class.java)
    }

    @Test
    @DisplayName("non-number JSON Schema properties should prevent schema recognition")
    internal fun test8() {

        assertThrows<UnrecognizableJsonSchemaException> {

            OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
              "multipleOf" : 3.14,
              "maxLength": 2
            }
            """.trimIndent())
        }
    }

    @Test
    @DisplayName("known string JSON Schema properties should trigger schema recognition")
    internal fun test9() {

        var jsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
              "minLength" : 2
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(StringJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "maxLength" : 2
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(StringJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "pattern" : "test.*"
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(StringJsonSchemaNode::class.java)

        jsonSchemaNode = OBJECT_MAPPER.readValue("""
            {
              "format" : "email"
            }
        """.trimIndent())

        assertThat(jsonSchemaNode).isInstanceOf(StringJsonSchemaNode::class.java)
    }

    @Test
    @DisplayName("non-string JSON Schema properties should prevent schema recognition")
    internal fun test10() {

        assertThrows<UnrecognizableJsonSchemaException> {

            OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
              "maxLength": 2,
              "exclusiveMaximum" : 42
            }
            """.trimIndent())
        }
    }
}