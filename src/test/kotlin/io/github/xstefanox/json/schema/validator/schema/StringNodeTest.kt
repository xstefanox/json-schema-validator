package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
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
        assertThat(validationResult.errors).isEmpty()
    }

    @Test
    @DisplayName("non-string value should not be accepted")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string"
            }
            """)

        val json = OBJECT_MAPPER.readTree("123")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be a string")
                .pointsTo("/")
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
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("string should be at least 10 characters long")
                .pointsTo("/")
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
        assertThat(validationResult.errors).isEmpty()
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
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("string should be at most 3 characters long")
                .pointsTo("/")
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
        assertThat(validationResult.errors).isEmpty()
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
        assertThat(validationResult.errors).isEmpty()
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
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element does not match pattern")
                .pointsTo("/")
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
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be a string")
                .pointsTo("/")
    }

    @Test
    @DisplayName("date element should be accepted as ISO8601 date if type format is date")
    internal fun test10() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "date"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "2018-03-10+01:00"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)
        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("date-time element should be rejected as ISO8601 date if type format is date")
    internal fun test11() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "date"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "2018-03-10T17:54:20+01:00"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)
        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be a date")
                .pointsTo("/")
    }

    @Test
    @DisplayName("time element should be rejected as ISO8601 date if type format is date")
    internal fun test12() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "date"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "17:54:20+01:00"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)
        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be a date")
                .pointsTo("/")
    }

    @Test
    @DisplayName("time element should be accepted as ISO8601 time if type format is time")
    internal fun test13() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "time"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "17:54:20+01:00"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)
        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("date-time element should be rejected as ISO8601 date if type format is time")
    internal fun test14() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "time"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "2018-03-10T17:54:20+01:00"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)
        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be a time")
                .pointsTo("/")
    }

    @Test
    @DisplayName("date element should be rejected as ISO8601 date if type format is time")
    internal fun test15() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "time"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "2018-03-10"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)
        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be a time")
                .pointsTo("/")
    }

    @Test
    @DisplayName("date-time element should be accepted as ISO8601 date-time if type format is date-time")
    internal fun test16() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "date-time"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "2018-03-10T17:54:20+01:00"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)
        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("time element should be rejected as ISO8601 date-time if type format is date-time")
    internal fun test17() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "date-time"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "17:54:20+01:00"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)
        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be a date-time")
                .pointsTo("/")
    }

    @Test
    @DisplayName("date element should be rejected as ISO8601 date-time if type format is date-time")
    internal fun test18() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "date-time"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "2018-03-10"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)
        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be a date-time")
                .pointsTo("/")
    }

    @Test
    @DisplayName("valid email address should be accepted if type format is email")
    internal fun test19() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "email"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "leeroy.jenkins@example.org"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("invalid email address should be rejected if type format is email")
    internal fun test20() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "email"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "THIS IS NOT A VALID EMAIL ADDRESS"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be an email address")
                .pointsTo("/")
    }

    @Test
    @DisplayName("valid host name should be accepted if type format is hostname")
    internal fun test21() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "hostname"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "this.is.a.valid.hostname"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isTrue()
    }

    @Test
    @DisplayName("invalid host name should be rejected if type format is hostname")
    internal fun test22() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "string",
                "format": "hostname"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "THIS IS NOT A VALID HOST NAME"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be a host name")
                .pointsTo("/")
    }
}