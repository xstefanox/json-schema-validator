package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(validationResult.isSuccessful).isTrue()
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

        assertThat(validationResult.isSuccessful).isFalse()
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

        assertThat(validationResult.isSuccessful).isFalse()
    }

    @Test
    @DisplayName("integer lesser than lower bound should be rejected")
    internal fun test4() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "minimum": 5
            }
            """)

        val json = OBJECT_MAPPER.readTree("2")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
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

        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("lower bound should be exclusive if requested")
    internal fun test6() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "minimum": 5,
                "exclusiveMinimum": true
            }
            """)

        val json = OBJECT_MAPPER.readTree("5")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
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

        assertThat(validationResult.isSuccessful).isFalse()
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

        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("upper bound should be exclusive if requested")
    internal fun test9() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "integer",
                "maximum": 5,
                "exclusiveMaximum": true
            }
            """)

        val json = OBJECT_MAPPER.readTree("5")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
    }
}