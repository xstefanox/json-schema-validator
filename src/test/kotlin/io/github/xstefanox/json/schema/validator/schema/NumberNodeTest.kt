package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(validationResult.isSuccessful).isTrue()
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

        assertThat(validationResult.isSuccessful).isFalse()
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

        assertThat(validationResult.isSuccessful).isFalse()
    }

    @Test
    @DisplayName("number lesser than lower bound should be rejected")
    internal fun test4() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "minimum": 5.1
            }
            """)

        val json = OBJECT_MAPPER.readTree("3.14")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
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

        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("lower bound should be exclusive if requested")
    internal fun test6() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "minimum": 3.14,
                "exclusiveMinimum": true
            }
            """)

        val json = OBJECT_MAPPER.readTree("3.14")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
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

        assertThat(validationResult.isSuccessful).isFalse()
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

        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("upper bound should be exclusive if requested")
    internal fun test9() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "number",
                "maximum": 3.14,
                "exclusiveMaximum": true
            }
            """)

        val json = OBJECT_MAPPER.readTree("3.14")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
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

        assertThat(validationResult.isSuccessful).isFalse()

        assertThat(validationResult.errors)
                .describedAs("the validation should produce only one error")
                .hasSize(1)
    }
}