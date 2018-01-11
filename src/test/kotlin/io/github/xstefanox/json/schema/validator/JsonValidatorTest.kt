package io.github.xstefanox.json.schema.validator

import TestUtils.Companion.OBJECT_MAPPER
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class JsonValidatorTest {

    @Test
    @DisplayName("boolean true Schema should validate anything")
    internal fun test1() {

        val jsonSchema = BooleanJsonSchema(true)
        val jsonNode = OBJECT_MAPPER.readTree("{}")

        assertTrue(JsonValidator().validate(jsonSchema, jsonNode))
    }

    @Test
    @DisplayName("boolean false Schema should validate nothing")
    internal fun test2() {

        val jsonSchema = BooleanJsonSchema(false)
        val jsonNode = OBJECT_MAPPER.readTree("{}")

        assertFalse(JsonValidator().validate(jsonSchema, jsonNode))
    }
}