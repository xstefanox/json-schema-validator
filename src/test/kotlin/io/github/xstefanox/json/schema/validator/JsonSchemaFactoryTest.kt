package io.github.xstefanox.json.schema.validator

import io.github.xstefanox.json.schema.validator.JsonSchema.Companion.DEFAULT_SCHEMA_URI
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.net.URI

internal class JsonSchemaFactoryTest {

    @Test
    @DisplayName("null \$schema should be converted to default")
    internal fun test1() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "${'$'}schema": null,
                "type": "boolean"
            }
            """)

        assertThat(jsonSchema.schema).isEqualTo(DEFAULT_SCHEMA_URI)
    }

    @Test
    @DisplayName("missing \$schema should be converted to default")
    internal fun test2() {

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "type": "boolean"
            }
            """)

        assertThat(jsonSchema.schema).isEqualTo(DEFAULT_SCHEMA_URI)
    }

    @Test
    @DisplayName("invalid \$schema should be rejected")
    internal fun test3() {

        assertThrows(IllegalArgumentException::class.java, {
            JsonSchemaFactory().from("""
            {
                "${'$'}schema": "THIS IS NOT A VALID URI",
                "type": "boolean"
            }
            """)
        })
    }

    @Test
    @DisplayName("valid \$schema should be readable")
    internal fun test4() {

        val schemaUri = URI.create("http://example.org/a/valid/path")

        val jsonSchema = JsonSchemaFactory().from("""
            {
                "${'$'}schema": "$schemaUri",
                "type": "boolean"
            }
            """)

        assertThat(jsonSchema.schema).isEqualTo(schemaUri)
    }
}