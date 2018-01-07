import io.github.xstefanox.json.schema.validator.JsonSchema
import io.github.xstefanox.json.schema.validator.JsonSchemaType.NUMBER
import io.github.xstefanox.json.schema.validator.OBJECT_MAPPER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.net.URI

class JsonSchemaTest {

    @Test
    @DisplayName("description should be readable")
    internal fun test1() {

        val description = "TEST_DESCRIPTION"

        val jsonNode = OBJECT_MAPPER.readTree("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "description": "$description",
                "type": "boolean"
            }
            """)

        val jsonSchema = JsonSchema(jsonNode)

        assertThat(jsonSchema.description)
                .isEqualTo(description)
    }

    @Test
    @DisplayName("array node should be rejected")
    internal fun test2() {
        assertThrows(IllegalArgumentException::class.java, {
            JsonSchema(OBJECT_MAPPER.readTree("[]"))
        })
    }

    @Test
    @DisplayName("null node should be rejected")
    internal fun test3() {
        assertThrows(IllegalArgumentException::class.java, {
            JsonSchema(OBJECT_MAPPER.readTree("null"))
        })
    }

    @Test
    @DisplayName("missing description should be null")
    internal fun test4() {

        val jsonNode = OBJECT_MAPPER.readTree("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": "boolean"
            }
            """)

        val jsonSchema = JsonSchema(jsonNode)

        assertThat(jsonSchema.description)
                .isNull()
    }

    @Test
    @DisplayName("null description should be null")
    internal fun test5() {

        val jsonNode = OBJECT_MAPPER.readTree("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "description": null,
                "type": "boolean"
            }
            """)

        val jsonSchema = JsonSchema(jsonNode)

        assertThat(jsonSchema.description)
                .isNull()
    }

    @Test
    @DisplayName("null type should be rejected")
    internal fun test6() {

        assertThrows(IllegalArgumentException::class.java, {
            JsonSchema(OBJECT_MAPPER.readTree("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": null
            }
            """))
        })
    }

    @Test
    @DisplayName("invalid type should be rejected")
    internal fun test7() {

        assertThrows(IllegalArgumentException::class.java, {
            JsonSchema(OBJECT_MAPPER.readTree("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": "TEST"
            }
            """))
        })
    }

    @Test
    @DisplayName("type should be readable")
    internal fun test8() {

        val type = "number"

        val jsonSchema = JsonSchema(OBJECT_MAPPER.readTree("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": "$type"
            }
            """))

        assertThat(jsonSchema.type).isEqualTo(NUMBER)
    }

    @Test
    @DisplayName("type detection should be case sensitive")
    internal fun test9() {

        assertThrows(IllegalArgumentException::class.java, {
            JsonSchema(OBJECT_MAPPER.readTree("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": "BOOLEAN"
            }
            """))
        })
    }

    @Test
    @DisplayName("null \$schema should be rejected")
    internal fun test10() {

        assertThrows(IllegalArgumentException::class.java, {
            JsonSchema(OBJECT_MAPPER.readTree("""
            {
                "${'$'}schema": null,
                "type": "boolean"
            }
            """))
        })
    }

    @Test
    @DisplayName("missing \$schema should be rejected")
    internal fun test11() {

        assertThrows(IllegalArgumentException::class.java, {
            JsonSchema(OBJECT_MAPPER.readTree("""
            {
                "type": "boolean"
            }
            """))
        })
    }

    @Test
    @DisplayName("invalid \$schema should be rejected")
    internal fun test12() {

        assertThrows(IllegalArgumentException::class.java, {
            JsonSchema(OBJECT_MAPPER.readTree("""
            {
                "${'$'}schema": "THIS IS NOT A VALID URI",
                "type": "boolean"
            }
            """))
        })
    }

    @Test
    @DisplayName("valid \$schema should be readable")
    internal fun test13() {

        val schemaUri = URI.create("http://example.org/a/valid/path")

        val jsonSchema = JsonSchema(OBJECT_MAPPER.readTree("""
            {
                "${'$'}schema": "http://example.org/a/valid/path",
                "type": "boolean"
            }
            """))

        assertThat(jsonSchema.schema).isEqualTo(schemaUri)
    }
}