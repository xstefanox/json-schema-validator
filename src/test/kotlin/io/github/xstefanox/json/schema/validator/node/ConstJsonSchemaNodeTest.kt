package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ConstJsonSchemaNodeTest {

    @Test
    @DisplayName("string const JSON Schema can be deserialized")
    internal fun test1() {

        val constJsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
                "const": "THIS IS A CONSTANT VALUE"
            }
        """)

        assertThat(constJsonSchemaNode).isEqualTo(StringConstJsonSchemaNode("THIS IS A CONSTANT VALUE"))
    }

    @Test
    @DisplayName("int const JSON Schema can be deserialized")
    internal fun test2() {

        val constJsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
                "const": 42
            }
        """)

        assertThat(constJsonSchemaNode).isEqualTo(IntegerConstJsonSchemaNode(42))
    }

    @Test
    @DisplayName("number const JSON Schema can be deserialized")
    internal fun test3() {

        val constJsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
                "const": 3.14
            }
        """)

        assertThat(constJsonSchemaNode).isEqualTo(NumberConstJsonSchemaNode(3.14))
    }

    @Test
    @DisplayName("array const JSON Schema can be deserialized")
    internal fun test4() {

        val constJsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
                "const": [
                    "red",
                    "green"
                ]
            }
        """)

        assertThat(constJsonSchemaNode).isEqualTo(ArrayConstJsonSchemaNode(listOf("red", "green")))
    }

    @Test
    @DisplayName("null const JSON Schema can be deserialized")
    internal fun test5() {

        val constJsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
                "const": null
            }
        """)

        assertThat(constJsonSchemaNode).isEqualTo(NullConstJsonSchemaNode)
    }

    @Test
    @DisplayName("boolean const JSON Schema can be deserialized")
    internal fun test6() {

        val constJsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
                "const": true
            }
        """)

        assertThat(constJsonSchemaNode).isEqualTo(BooleanConstJsonSchemaNode(true))
    }

    @Test
    @DisplayName("object const JSON Schema can be deserialized")
    internal fun test7() {

        val constJsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
                "const": {
                    "firstName": "Leeroy",
                    "lastName": "Jenkins"
                }
            }
        """)

        val expected = ObjectConstJsonSchemaNode(
                JsonNodeFactory.instance
                        .objectNode()
                        .put("firstName", "Leeroy")
                        .put("lastName", "Jenkins")
        )

        assertThat(constJsonSchemaNode).isEqualTo(expected)
    }

    @Test
    @DisplayName("string const JSON Schema should be serialized")
    internal fun test8() {

        val serializedNode = OBJECT_MAPPER.writeValueAsString(StringConstJsonSchemaNode("THIS IS A CONSTANT VALUE"))

        assertThat(serializedNode).isEqualTo("""
            {
              "const" : "THIS IS A CONSTANT VALUE"
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("int const JSON Schema can be serialized")
    internal fun test9() {

        val serializedNode = OBJECT_MAPPER.writeValueAsString(IntegerConstJsonSchemaNode(42))

        assertThat(serializedNode).isEqualTo("""
            {
              "const" : 42
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("number const JSON Schema can be serialized")
    internal fun test10() {

        val serializedNode = OBJECT_MAPPER.writeValueAsString(NumberConstJsonSchemaNode(3.14))

        assertThat(serializedNode).isEqualTo("""
            {
              "const" : 3.14
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("array const JSON Schema can be serialized")
    internal fun test11() {

        val serializedNode = OBJECT_MAPPER.writeValueAsString(ArrayConstJsonSchemaNode(listOf("red", "green")))

        assertThat(serializedNode).isEqualTo("""
            {
              "const" : [ "red", "green" ]
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("null const JSON Schema can be serialized")
    internal fun test12() {

        val serializedNode = OBJECT_MAPPER.writeValueAsString(NullConstJsonSchemaNode)

        assertThat(serializedNode).isEqualTo("""
            {
              "const" : null
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("boolean const JSON Schema can be serialized")
    internal fun test13() {

        val serializedNode = OBJECT_MAPPER.writeValueAsString(BooleanConstJsonSchemaNode(true))

        assertThat(serializedNode).isEqualTo("""
            {
              "const" : true
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("object const JSON Schema can be serialized")
    internal fun test14() {

        val serializedNode = OBJECT_MAPPER.writeValueAsString(ObjectConstJsonSchemaNode(
                JsonNodeFactory.instance
                        .objectNode()
                        .put("firstName", "Leeroy")
                        .put("lastName", "Jenkins")
        ))

        assertThat(serializedNode).isEqualTo("""
            {
              "const" : {
                "firstName" : "Leeroy",
                "lastName" : "Jenkins"
              }
            }
            """.trimIndent())
    }
}