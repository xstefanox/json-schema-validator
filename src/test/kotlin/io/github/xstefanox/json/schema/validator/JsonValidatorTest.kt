package io.github.xstefanox.json.schema.validator

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.databind.node.ObjectNode
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations.initMocks

internal class JsonValidatorTest {

    @InjectMocks
    private lateinit var jsonValidator : JsonValidator

    @BeforeEach
    internal fun setUp() {
        initMocks(this)
    }

    @Test
    @DisplayName("boolean true Schema should validate anything")
    internal fun test1() {

        val jsonSchema = BooleanJsonSchema(true)
        val jsonNode = OBJECT_MAPPER.readTree("{}")

        assertTrue(jsonValidator.validate(jsonSchema, jsonNode))
    }

    @Test
    @DisplayName("boolean false Schema should validate nothing")
    internal fun test2() {

        val jsonSchema = BooleanJsonSchema(false)
        val jsonNode = OBJECT_MAPPER.readTree("{}")

        assertFalse(jsonValidator.validate(jsonSchema, jsonNode))
    }

    @Test
    @DisplayName("empty schema should validate anything")
    internal fun test3() {

        val jsonSchema = ObjectNodeJsonSchema(OBJECT_MAPPER.readValue("{}", ObjectNode::class.java))
        val jsonNode = OBJECT_MAPPER.readTree("{}")

        assertTrue(jsonValidator.validate(jsonSchema, jsonNode))
    }
}