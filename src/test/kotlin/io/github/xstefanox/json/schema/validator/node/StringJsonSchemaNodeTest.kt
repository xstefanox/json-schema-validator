package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.xstefanox.json.schema.validator.model.PositiveInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class StringJsonSchemaNodeTest {

    @Test
    @DisplayName("string JSON Schema node must be serialized")
    internal fun test1() {

        assertThat(OBJECT_MAPPER.writeValueAsString(StringJsonSchemaNode())).isEqualTo("""
            {
              "type" : "string",
              "minLength" : 0,
              "maxLength" : null,
              "pattern" : null,
              "format" : null
            }
            """.trimIndent())
    }

    @Test
    @DisplayName("string JSON Schema node must be deserialized")
    internal fun test2() {

        assertThat(OBJECT_MAPPER.readValue<JsonSchemaNode>("""
            {
              "type" : "string",
              "minLength" : 1,
              "maxLength" : 2,
              "pattern" : null,
              "format" : null
            }
            """)).isEqualTo(StringJsonSchemaNode(PositiveInt(1), PositiveInt(2)))
    }

    @Test
    @DisplayName("minLEngth must be greater or equal to 0")
    internal fun test3() {
        assertThrows(IllegalArgumentException::class.java, { StringJsonSchemaNode(minLength = PositiveInt(-1)) })
    }

    @Test
    @DisplayName("maxLength must be greater or equal to 0")
    internal fun test4() {
        assertThrows(IllegalArgumentException::class.java, { StringJsonSchemaNode(maxLength = PositiveInt(-1)) })
    }

    @Test
    @DisplayName("maxLength must be greater or equal to minLength")
    internal fun test5() {
        assertThrows(IllegalArgumentException::class.java, { StringJsonSchemaNode(minLength = PositiveInt(1), maxLength = PositiveInt(0)) })
    }
}