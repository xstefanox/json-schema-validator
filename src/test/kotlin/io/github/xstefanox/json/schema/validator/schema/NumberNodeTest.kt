package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class NumberNodeTest {

    @Test
    @DisplayName("number property should be validated")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number"
            }
            """)

        val json = OBJECT_MAPPER.readTree("3.14")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("non number property should not be validated")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number"
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
                .hasMessage("element should be a number")
                .pointsTo("/")
    }

    @Test
    @DisplayName("non multiple number should be rejected")
    internal fun test3() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "multipleOf": 1.5
            }
            """)

        val json = OBJECT_MAPPER.readTree("3.14")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be a multiple of 1.5")
                .pointsTo("/")
    }

    @Test
    @DisplayName("number less than lower bound should be rejected")
    internal fun test4() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "minimum": 5.1
            }
            """)

        val json = OBJECT_MAPPER.readTree("3.14")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be greater than or equal to 5.1")
                .pointsTo("/")
    }

    @Test
    @DisplayName("lower bound should be inclusive by default")
    internal fun test5() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "minimum": 3.14
            }
            """)

        val json = OBJECT_MAPPER.readTree("3.14")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("lower bound should be exclusive if requested")
    internal fun test6() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "exclusiveMinimum": 3.14
            }
            """)

        val json = OBJECT_MAPPER.readTree("3.14")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be greater than 3.14")
                .pointsTo("/")
    }

    @Test
    @DisplayName("number greater than upper bound should be rejected")
    internal fun test7() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "maximum": 3.14
            }
            """)

        val json = OBJECT_MAPPER.readTree("7.1")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be less than or equal to 3.14")
                .pointsTo("/")
    }

    @Test
    @DisplayName("upper bound should be inclusive by default")
    internal fun test8() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "maximum": 3.14
            }
            """)

        val json = OBJECT_MAPPER.readTree("3.14")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("upper bound should be exclusive if requested")
    internal fun test9() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "exclusiveMaximum": 3.14
            }
            """)

        val json = OBJECT_MAPPER.readTree("3.14")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be less than 3.14")
                .pointsTo("/")
    }

    @Test
    @DisplayName("if the element is not a number, no further validation should be performed")
    internal fun test10() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "minimum": 3.14
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
                .hasMessage("element should be a number")
                .pointsTo("/")
    }
}