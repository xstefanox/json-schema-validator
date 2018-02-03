package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class IntegerJsonSchemaNodeTest {

    @Test
    @DisplayName("integer type should be deserialized")
    internal fun test1() {
        OBJECT_MAPPER.readValue<IntegerJsonSchemaNode>("""
            {
                "type": "integer"
            }
            """)
    }

    @Test
    @DisplayName("integer type should be serialized")
    internal fun test2() {

        val integerJsonSchemaNode = IntegerJsonSchemaNode()

        assertThat(OBJECT_MAPPER.writeValueAsString(integerJsonSchemaNode)).isEqualTo("""
            {
              "type" : "integer",
              "multipleOf" : null,
              "minimum" : null,
              "maximum" : null,
              "exclusiveMinimum" : false,
              "exclusiveMaximum" : false
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("maximum bound should be greater or equal to minimum bound")
    internal fun test3() {
        assertThrows(IllegalArgumentException::class.java, { IntegerJsonSchemaNode(null, 2, 1) })
    }
}