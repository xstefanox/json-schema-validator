package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class OneOfJsonSchemaTest {

    @Test
    @DisplayName("value should be accepted if it does conform to exactly one of the nested schema")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "oneOf": [
                    {
                        "type": "integer"
                    },
                    {
                        "type": "string"
                    },
                    {
                        "type": "null"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "TEST"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("value should not be accepted if it does not conform to any of the nested schema")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "oneOf": [
                    {
                        "type": "integer"
                    },
                    {
                        "type": "string"
                    },
                    {
                        "type": "null"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            true
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element does not match any of the nested schema")
                .pointsTo("/")
    }

    @Test
    @DisplayName("value should not be accepted if it does conform to more than one of the nested schema")
    internal fun test3() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "oneOf": [
                    {
                        "type": "integer"
                    },
                    {
                        "type": "string",
                        "minLength": 2
                    },
                    {
                        "type": "string",
                        "maxLength": 4
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "TEST"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element matches more than one of the nested schema")
                .pointsTo("/")
    }
}