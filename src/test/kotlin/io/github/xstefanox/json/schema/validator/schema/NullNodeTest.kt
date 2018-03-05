package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class NullNodeTest {

    @Test
    @DisplayName("null value should be accepted")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "null"
            }
            """)

        val json = OBJECT_MAPPER.readTree("null")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isTrue()
        assertThat(validationResult.errors).isEmpty()
    }

    @Test
    @DisplayName("non-null value should be rejected")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "null"
            }
            """)

        val json = OBJECT_MAPPER.readTree("123")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult.isSuccessful).isFalse()
        assertThat(validationResult.errors).hasSize(1)
        assertThat(validationResult.errors[0]).hasMessage().pointsTo("/")
    }
}