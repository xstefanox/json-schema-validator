package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.xstefanox.json.schema.validator.model.PositiveInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ArrayJsonSchemaNodeTest {

    @Test
    @DisplayName("maxItems must be greater than or equal to minItems")
    internal fun test1() {
        assertThrows(IllegalArgumentException::class.java, {
            ArrayJsonSchemaNode(
                    minItems = PositiveInt(2),
                    maxItems = PositiveInt(1)
            )
        })
    }

    @Test
    @DisplayName("array JSON Schema node can be serialized")
    internal fun test2() {

        val serializedNode = OBJECT_MAPPER.writeValueAsString(ArrayJsonSchemaNode())

        assertThat(serializedNode).isEqualTo("""
            {
              "type" : "array",
              "items" : null,
              "minItems" : 0,
              "maxItems" : null,
              "uniqueItems" : false
            }
        """.trimIndent())
    }

    @Test
    @DisplayName("array JSON Schema can be deserialized")
    internal fun test3() {

        val arrayJsonSchemaNode = OBJECT_MAPPER.readValue<ArrayJsonSchemaNode>("""
            {
              "type" : "array",
              "items" : null,
              "minItems" : 1,
              "maxItems" : 2,
              "uniqueItems" : true
            }
        """)

        assertThat(arrayJsonSchemaNode).isEqualTo(ArrayJsonSchemaNode(
                minItems = PositiveInt(1),
                maxItems = PositiveInt(2),
                uniqueItems = true)
        )
    }

    @Test
    @DisplayName("array JSON Schema nodes having the same properties must be equal")
    internal fun test4() {

        val arrayJsonSchemaNode1 = ArrayJsonSchemaNode(minItems = PositiveInt(1))
        val arrayJsonSchemaNode2 = ArrayJsonSchemaNode(minItems = PositiveInt(1))

        assertThat(arrayJsonSchemaNode1).isEqualTo(arrayJsonSchemaNode2)
    }
}