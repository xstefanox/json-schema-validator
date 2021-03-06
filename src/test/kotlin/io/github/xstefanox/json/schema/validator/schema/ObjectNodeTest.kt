package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ObjectNodeTest {

    @Test
    @DisplayName("object elements should be accepted")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object"
            }
            """)

        val json = OBJECT_MAPPER.readTree("{}")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("non-object elements should not be accepted")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object"
            }
            """)

        val json = OBJECT_MAPPER.readTree("true")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be an object")
                .pointsTo("/")
    }

    @Test
    @DisplayName("if the element is not an object, no further validation should be performed")
    internal fun test3() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "minProperties": 10
            }
            """)

        val json = OBJECT_MAPPER.readTree("true")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be an object")
                .pointsTo("/")
    }

    @Test
    @DisplayName("the object should be rejected if at least one property does not conform to the schema")
    internal fun test4() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "properties": {
                    "firstName": {
                        "type": "string"
                    },
                    "lastName": {
                        "type": "string"
                    }
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": 42,
                "lastName": "Jenkins"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should be a string")
                .pointsTo("/firstName")
    }

    @Test
    @DisplayName("the object should be accepted if all properties conform to the schema")
    internal fun test5() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "properties": {
                    "firstName": {
                        "type": "string"
                    },
                    "lastName": {
                        "type": "string"
                    }
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
    @DisplayName("the object should be rejected if at least one required property is missing")
    internal fun test6() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "required": [
                    "firstName",
                    "lastName"
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "leeroy"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("property is missing")
                .pointsTo("/lastName")
    }

    @Test
    @DisplayName("the object should be accepted if all required properties are present")
    internal fun test7() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "required": [
                    "firstName",
                    "lastName"
                ]
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "leeroy",
                "lastName": "Jenkins"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("the object should be rejected if additional properties are not allowed and an additional property if present")
    internal fun test8() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "properties": {
                    "firstName": {
                        "type": "string"
                    },
                    "lastName": {
                        "type": "string"
                    }
                },
                "additionalProperties": false
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "leeroy",
                "lastName": "Jenkins",
                "address": "WoW"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("additional properties not allowed")
                .pointsTo("/address")
    }

    @Test
    @DisplayName("the object should be accepted if additional properties are allowed and an additional property if present")
    internal fun test9() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "properties": {
                    "firstName": {
                        "type": "string"
                    },
                    "lastName": {
                        "type": "string"
                    }
                },
                "additionalProperties": true
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "leeroy",
                "lastName": "Jenkins",
                "address": "WoW"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("object should be rejected if minProperties is not respected")
    internal fun test10() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "minProperties": 3
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "leeroy",
                "lastName": "Jenkins"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should have at least 3 properties")
                .pointsTo("/")
    }

    @Test
    @DisplayName("minProperties should be inclusive")
    internal fun test11() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "minProperties": 3
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "leeroy",
                "lastName": "Jenkins",
                "address": "WoW"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("object should be rejected if maxProperties is not respected")
    internal fun test12() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "maxProperties": 2
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "leeroy",
                "lastName": "Jenkins",
                "address": "WoW"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should have at most 2 properties")
                .pointsTo("/")
    }

    @Test
    @DisplayName("maxProperties should be inclusive")
    internal fun test13() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "maxProperties": 3
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": "leeroy",
                "lastName": "Jenkins",
                "address": "WoW"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("when an object is rejected all the non conforming properties should be reported")
    internal fun test14() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "object",
                "properties": {
                    "firstName": {
                        "type": "string"
                    },
                    "lastName": {
                        "type": "string"
                    },
                    "age": {
                        "type": "integer"
                    }
                },
                "additionalProperties": false
            }
            """)

        val json = OBJECT_MAPPER.readTree("""
            {
                "firstName": 42,
                "lastName": false,
                "age": "test",
                "something": "is broken"
            }
        """.trimIndent())

        val validationResult = jsonSchema.validate(json)

        // @formatter:off

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(4)
                .at(0)
                    .hasMessage("element should be a string")
                    .pointsTo("/firstName")
                .at(1)
                    .hasMessage("element should be a string")
                    .pointsTo("/lastName")
                .at(2)
                    .hasMessage("element should be an integer")
                    .pointsTo("/age")
                .at(3)
                    .hasMessage("additional properties not allowed")
                    .pointsTo("/something")

        // @formatter:on
    }
}