package io.github.xstefanox.json.schema.validator

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.xstefanox.json.schema.validator.JsonSchemaType.NUMBER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.net.URI

class JsonSchemaTest {

    @Test
    @DisplayName("description should be readable")
    internal fun test1() {

        val description = "TEST_DESCRIPTION"

        val jsonNode = OBJECT_MAPPER.readValue("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "description": "$description",
                "type": "boolean"
            }
            """, ObjectNode::class.java)

        val jsonSchema = ObjectNodeJsonSchema(jsonNode)

        assertThat(jsonSchema.description)
                .isEqualTo(description)
    }

    @Test
    @DisplayName("missing description should be null")
    internal fun test4() {

        val jsonNode = OBJECT_MAPPER.readValue("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": "boolean"
            }
            """, ObjectNode::class.java)

        val jsonSchema = ObjectNodeJsonSchema(jsonNode)

        assertThat(jsonSchema.description)
                .isNull()
    }

    @Test
    @DisplayName("null description should be null")
    internal fun test5() {

        val jsonNode = OBJECT_MAPPER.readValue("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "description": null,
                "type": "boolean"
            }
            """, ObjectNode::class.java)

        val jsonSchema = ObjectNodeJsonSchema(jsonNode)

        assertThat(jsonSchema.description)
                .isNull()
    }

    @Test
    @DisplayName("null type should be rejected")
    internal fun test6() {

        assertThrows(IllegalArgumentException::class.java, {
            ObjectNodeJsonSchema(OBJECT_MAPPER.readValue("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": null
            }
            """, ObjectNode::class.java))
        })
    }

    @Test
    @DisplayName("invalid type should be rejected")
    internal fun test7() {

        assertThrows(IllegalArgumentException::class.java, {
            ObjectNodeJsonSchema(OBJECT_MAPPER.readValue("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": "TEST"
            }
            """, ObjectNode::class.java))
        })
    }

    @Test
    @DisplayName("type should be readable")
    internal fun test8() {

        val type = "number"

        val jsonSchema = ObjectNodeJsonSchema(OBJECT_MAPPER.readValue("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": "$type"
            }
            """, ObjectNode::class.java))

        assertThat(jsonSchema.type).isEqualTo(NUMBER)
    }

    @Test
    @DisplayName("type detection should be case sensitive")
    internal fun test9() {

        assertThrows(IllegalArgumentException::class.java, {
            ObjectNodeJsonSchema(OBJECT_MAPPER.readValue("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": "BOOLEAN"
            }
            """, ObjectNode::class.java))
        })
    }

    @Test
    @DisplayName("null \$schema should be rejected")
    internal fun test10() {

        assertThrows(IllegalArgumentException::class.java, {
            ObjectNodeJsonSchema(OBJECT_MAPPER.readValue("""
            {
                "${'$'}schema": null,
                "type": "boolean"
            }
            """, ObjectNode::class.java))
        })
    }

    @Test
    @DisplayName("missing \$schema should be rejected")
    internal fun test11() {

        assertThrows(IllegalArgumentException::class.java, {
            ObjectNodeJsonSchema(OBJECT_MAPPER.readValue("""
            {
                "type": "boolean"
            }
            """, ObjectNode::class.java))
        })
    }

    @Test
    @DisplayName("invalid \$schema should be rejected")
    internal fun test12() {

        assertThrows(IllegalArgumentException::class.java, {
            ObjectNodeJsonSchema(OBJECT_MAPPER.readValue("""
            {
                "${'$'}schema": "THIS IS NOT A VALID URI",
                "type": "boolean"
            }
            """, ObjectNode::class.java))
        })
    }

    @Test
    @DisplayName("valid \$schema should be readable")
    internal fun test13() {

        val schemaUri = URI.create("http://example.org/a/valid/path")

        val jsonSchema = ObjectNodeJsonSchema(OBJECT_MAPPER.readValue("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": "boolean"
            }
            """, ObjectNode::class.java))

        assertThat(jsonSchema.schema).isEqualTo(schemaUri)
    }
}