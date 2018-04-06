package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class NumberJsonSchemaNodeTest {

    @Test
    @DisplayName("number type should be deserialized")
    internal fun test1() {
        OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
                "type": "number"
            }
            """)
    }

    @Test
    @DisplayName("number type should be serialized")
    internal fun test2() {

        val numberJsonSchemaNode = NumberJsonSchemaNode()

        assertThat(OBJECT_MAPPER.writeValueAsString(numberJsonSchemaNode)).isEqualTo("""
            {
              "type" : "number",
              "multipleOf" : null,
              "minimum" : null,
              "maximum" : null,
              "exclusiveMinimum" : null,
              "exclusiveMaximum" : null
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("maximum bound should be greater or equal to minimum bound")
    internal fun test3() {
        assertThrows(IllegalArgumentException::class.java, { NumberJsonSchemaNode(null, 2.0, 1.0) })
    }
}