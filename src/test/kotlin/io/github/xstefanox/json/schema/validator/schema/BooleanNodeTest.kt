package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class BooleanNodeTest {

    @Test
    @DisplayName("true boolean property should be validated")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "boolean"
            }
            """)

        val json = OBJECT_MAPPER.readTree("true")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isTrue()
        assertThat(validationResult.errors).isEmpty()
    }

    @Test
    @DisplayName("false boolean property should be validated")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "boolean"
            }
            """)

        val json = OBJECT_MAPPER.readTree("false")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isTrue()
        assertThat(validationResult.errors).isEmpty()
    }

    @Test
    @DisplayName("non boolean element should not be validated")
    internal fun test3() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "boolean"
            }
            """)

        val json = OBJECT_MAPPER.readTree("123")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0])
                .hasMessage("element should be a boolean")
                .pointsTo("/")
    }
}