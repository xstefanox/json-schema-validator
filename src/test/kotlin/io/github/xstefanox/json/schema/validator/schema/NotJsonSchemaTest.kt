package io.github.xstefanox.json.schema.validator.schema

import TestUtils.Companion.OBJECT_MAPPER
import io.github.xstefanox.json.schema.validator.JsonSchemaFactory
import io.github.xstefanox.json.schema.validator.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class NotJsonSchemaTest {

    @Test
    @DisplayName("invalid value for negated schema should be accepted")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "not": {
                    "type": "integer"
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("null")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult).isSuccessful()
    }

    @Test
    @DisplayName("valid value for negated schema should not be accepted")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "not": {
                    "type": "integer"
                }
            }
            """)

        val json = OBJECT_MAPPER.readTree("42")

        val validationResult = jsonSchema.validate(json)

        assertThat(validationResult)
                .isNotSuccessful()
                .hasSize(1)
                .first()
                .hasMessage("element should not be valid")
    }
}
