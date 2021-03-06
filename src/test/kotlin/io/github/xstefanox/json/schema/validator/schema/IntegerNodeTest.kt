package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class IntegerNodeTest {

    @Test
    @DisplayName("integer property should be validated")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer"
            }
            """)

        val json = OBJECT_MAPPER.readTree("123")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("non integer property should not be validated")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer"
            }
            """)

        val json = OBJECT_MAPPER.readTree("1.0")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be an integer")
                .pointsTo("/")
    }

    @Test
    @DisplayName("non multiple integer should be rejected")
    internal fun test3() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "multipleOf": 3
            }
            """)

        val json = OBJECT_MAPPER.readTree("10")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be a multiple of 3")
                .pointsTo("/")
    }

    @Test
    @DisplayName("integer less than lower bound should be rejected")
    internal fun test4() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "minimum": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("2")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be greater than or equal to 5")
                .pointsTo("/")
    }

    @Test
    @DisplayName("lower bound should be inclusive by default")
    internal fun test5() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "minimum": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("5")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("lower bound should be exclusive if requested")
    internal fun test6() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "exclusiveMinimum": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("5")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be greater than 5")
                .pointsTo("/")
    }

    @Test
    @DisplayName("integer greater than upper bound should be rejected")
    internal fun test7() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "maximum": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("7")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be less than or equal to 5")
                .pointsTo("/")
    }

    @Test
    @DisplayName("upper bound should be inclusive by default")
    internal fun test8() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "maximum": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("5")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("upper bound should be exclusive if requested")
    internal fun test9() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "exclusiveMaximum": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("5")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be less than 5")
                .pointsTo("/")
    }

    @Test
    @DisplayName("if the element is not an integer, no further validation should be performed")
    internal fun test10() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "minimum": 42
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
                .hasMessage("element should be an integer")
                .pointsTo("/")
    }
}