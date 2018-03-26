package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class EnumJsonSchemaNodeTest {

    @Test
    @DisplayName("enum type should be deserialized")
    internal fun test1() {

        val jsonSchemaNode = OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
                "enum": [
                    42,
                    "TEST"
                ]
            }
            """)

        assertThat(jsonSchemaNode).isInstanceOf(EnumJsonSchemaNode::class.java)

        jsonSchemaNode as EnumJsonSchemaNode

        assertThat(jsonSchemaNode.enum).isEqualTo(listOf(
                42,
                "TEST"
        ))
    }

    @Test
    @DisplayName("enum type should be serialized")
    internal fun test2() {

        val enumJsonSchemaNode = EnumJsonSchemaNode(listOf(
                3.14,
                mapOf(
                        "firstName" to "Leeroy",
                        "lastName" to "Jenkins"
                )
        ))

        assertThat(OBJECT_MAPPER.writeValueAsString(enumJsonSchemaNode)).isEqualTo("""
            {
              "enum" : [ 3.14, {
                "firstName" : "Leeroy",
                "lastName" : "Jenkins"
              } ]
            }
            """.trimIndent())
    }
}