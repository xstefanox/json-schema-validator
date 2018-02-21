package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class StringNodeTest {

    @Test
    @DisplayName("string property should be accepted")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
                "test"
            """)

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("non-null value should not be accepted")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string"
            }
            """)

        val json = OBJECT_MAPPER.readTree("123")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
    }

    @Test
    @DisplayName("too short string should not be accepted")
    internal fun test3() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "minLength": 10
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
                "test"
            """)

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
    }

    @Test
    @DisplayName("string length lower bound should be inclusive")
    internal fun test4() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "minLength": 4
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
                "test"
            """)

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("too long string should not be accepted")
    internal fun test5() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "maxLength": 3
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
                "test"
            """)

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
    }

    @Test
    @DisplayName("string length upper bound should be inclusive")
    internal fun test6() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "maxLength": 4
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
                "test"
            """)

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("regex-matching string should be accepted")
    internal fun test7() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "pattern": "/^tes.*$/"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
                "test"
            """)

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("non regex-matching string should not be accepted")
    internal fun test8() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "pattern": "/^tes.*$/"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
                "aaa"
            """)

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
    }

    @Test
    @DisplayName("if the element is not a string, no further validation should be performed")
    internal fun test9() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "pattern": "/^tes.*${'$'}/"
            }
            """)

        val json = OBJECT_MAPPER.readTree("42")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()

        assertThat(validationResult.errors)
                .describedAs("the validation should produce only one error")
                .hasSize(1)
    }
}