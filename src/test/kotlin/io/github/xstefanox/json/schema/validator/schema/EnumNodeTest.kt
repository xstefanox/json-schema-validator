package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class EnumNodeTest {

    @Test
    @DisplayName("integer should be accepted if present in enum")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    true,
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            42
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("integer should not be accepted if present in enum")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    3.14,
                    "TEST",
                    true,
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            42
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element not found in enum")
                .pointsTo("/")
    }

    @Test
    @DisplayName("number should be accepted if present in enum")
    internal fun test3() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    true,
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            3.14
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("number should not be accepted if present in enum")
    internal fun test4() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    "TEST",
                    true,
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            3.14
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element not found in enum")
                .pointsTo("/")
    }

    @Test
    @DisplayName("string should be accepted if present in enum")
    internal fun test5() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    true,
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
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
    @DisplayName("string should not be accepted if present in enum")
    internal fun test6() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    true,
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
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
                .hasMessage("element not found in enum")
                .pointsTo("/")
    }

    @Test
    @DisplayName("boolean should be accepted if present in enum")
    internal fun test7() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    true,
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            true
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("boolean should not be accepted if present in enum")
    internal fun test8() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
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
                .hasMessage("element not found in enum")
                .pointsTo("/")
    }

    @Test
    @DisplayName("null should be accepted if present in enum")
    internal fun test9() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    true,
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            null
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("null should not be accepted if present in enum")
    internal fun test10() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    true,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            null
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element not found in enum")
                .pointsTo("/")
    }

    @Test
    @DisplayName("array should be accepted if present in enum")
    internal fun test11() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    true,
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                1,
                2,
                3
            ]
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("array should not be accepted if present in enum")
    internal fun test12() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    true,
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
                    }
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            [
                1,
                2,
                3
            ]
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element not found in enum")
                .pointsTo("/")
    }

    @Test
    @DisplayName("object should be accepted if present in enum")
    internal fun test13() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    true,
                    null,
                    [
                        1,
                        2,
                        3
                    ],
                    {
                        "firstName": "Leeroy",
                        "lastName": "Jenkins"
                    }
                ]
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
    @DisplayName("object should not be accepted if present in enum")
    internal fun test14() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "enum": [
                    42,
                    3.14,
                    "TEST",
                    true,
                    [
                        1,
                        2,
                        3
                    ]
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "Leeroy",
                "lastName": "Jenkins"
            }
            """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element not found in enum")
                .pointsTo("/")
    }
}