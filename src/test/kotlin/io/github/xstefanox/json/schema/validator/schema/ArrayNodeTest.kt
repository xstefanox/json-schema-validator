package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ArrayNodeTest {

    @Test
    @DisplayName("all items should conform to the given schema")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "boolean"
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                true,
                false
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("validation should fail if at least one item does not conform to the given schema")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "boolean"
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                true,
                false,
                1
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be a boolean")
                .pointsTo("/2")
    }

    @Test
    @DisplayName("items must not be validated if no schema is defined")
    internal fun test3() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                1,
                true,
                null
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("validation should fail if too few items are present")
    internal fun test4() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "boolean"
                },
                "minItems": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                true,
                false
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("expected at least 5 items")
                .pointsTo("/")
    }

    @Test
    @DisplayName("validation should succeed if the minimum number of items is respected")
    internal fun test5() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "boolean"
                },
                "minItems": 3
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                true,
                false,
                true,
                false
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("validation should fail if too many items are present")
    internal fun test6() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "boolean"
                },
                "maxItems": 3
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                true,
                false,
                true,
                false,
                false
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("expected at most 3 items")
                .pointsTo("/")
    }

    @Test
    @DisplayName("validation should succeed if the maximum number of items is respected")
    internal fun test7() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "boolean"
                },
                "maxItems": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                true,
                false,
                true
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("validation should fail if uniqueness is requested and duplicated items are present")
    internal fun test8() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "boolean"
                },
                "uniqueItems": true
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                true,
                false,
                true
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("duplicated element")
                .pointsTo("/2")
    }

    @Test
    @DisplayName("validation should succeed if uniqueness is requested and no duplicated items are present")
    internal fun test9() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "boolean"
                },
                "uniqueItems": true
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                true,
                false
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("the lower bound should be inclusive")
    internal fun test10() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "boolean"
                },
                "minItems": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                true,
                false,
                true,
                true,
                false
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("the upper bound should be inclusive")
    internal fun test11() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "boolean"
                },
                "maxItems": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                true,
                false,
                false,
                false,
                true
            ]
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("non-array items should be rejected")
    internal fun test12() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array"
            }
            """)

        val json = OBJECT_MAPPER.readTree("42")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be an array")
                .pointsTo("/")
    }

    @Test
    @DisplayName("validation should succeed if at least one item conforms to the contains JSON Schema")
    internal fun test13() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "integer"
                },
                "contains": {
                    "type": "integer",
                    "multipleOf": 3
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                1,
                2,
                3,
                4,
                5
            ]
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("validation should fail if no one item conforms to the contains JSON Schema")
    internal fun test14() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "array",
                "items": {
                    "type": "integer"
                },
                "contains": {
                    "type": "integer",
                    "multipleOf": 3
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                1,
                2,
                4,
                5
            ]
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .first()
                .hasMessage("""no item matching "contains" schema found""")
                .pointsTo("/")
    }
}