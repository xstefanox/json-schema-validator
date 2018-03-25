package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ConstNodeTest {

    @Test
    @DisplayName("const array should be accepted if equal to JSON Schema value")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": [
                    42,
                    "TEST",
                    true,
                    null
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                42,
                "TEST",
                true,
                null
            ]
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("const array should not be accepted if different from JSON Schema value")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": [
                    42,
                    "TEST",
                    true,
                    null
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""[
                42,
                "TEST",
                false,
                null
            ]
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isNotSuccessful()
    }

    @Test
    @DisplayName("const boolean should be accepted if equal to JSON Schema value")
    internal fun test3() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": true
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            true
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("const boolean should not be accepted if different from JSON Schema value")
    internal fun test4() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": true
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            false
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isNotSuccessful()
    }

    @Test
    @DisplayName("const integer should be accepted if equal to JSON Schema value")
    internal fun test56() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": 42
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            42
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("const integer should not be accepted if different from JSON Schema value")
    internal fun test6() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": 42
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            0
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isNotSuccessful()
    }

    @Test
    @DisplayName("const null should be accepted if equal to JSON Schema value")
    internal fun test7() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": null
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            null
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("const null should not be accepted if different from JSON Schema value")
    internal fun test8() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": null
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            42
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isNotSuccessful()
    }

    @Test
    @DisplayName("const number should be accepted if equal to JSON Schema value")
    internal fun test9() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": 3.14
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            3.14
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("const number should not be accepted if different from JSON Schema value")
    internal fun test10() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": 3.14
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            9.81
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isNotSuccessful()
    }

    @Test
    @DisplayName("const object should be accepted if equal to JSON Schema value")
    internal fun test11() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": {
                    "firstName": "Leeroy",
                    "lastName": "Jenkins"
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "Leeroy",
                "lastName": "Jenkins"
            }
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("const object should not be accepted if different from JSON Schema value")
    internal fun test12() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": {
                    "firstName": "Leeroy",
                    "lastName": "Jenkins"
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "Leeroy",
                "lastName": "FAIL"
            }
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isNotSuccessful()
    }

    @Test
    @DisplayName("const string should be accepted if equal to JSON Schema value")
    internal fun test13() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": "TEST"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "TEST"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("const string should not be accepted if different from JSON Schema value")
    internal fun test14() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "const": "TEST"
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            "FAIL"
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isNotSuccessful()
    }
}