package io.github.xstefanox.json.schema.validator.node

import io.github.xstefanox.json.schema.validator.model.PositiveInt
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ObjectJsonSchemaNodeTest {

    @Test
    @DisplayName("maxProperties must be greater than or equal to minProperties")
    internal fun test1() {
        assertThrows(IllegalArgumentException::class.java, {
            ObjectJsonSchemaNode(minProperties = PositiveInt(2), maxProperties = PositiveInt(1))
        })
    }

    @Test
    @DisplayName("maxProperties must be greater than or equal to required properties count")
    internal fun test2() {
        assertThrows(IllegalArgumentException::class.java, {
            ObjectJsonSchemaNode(
                    required = setOf(
                            ObjectJsonSchemaNode.Property("property1"),
                            ObjectJsonSchemaNode.Property("property2")
                    ),
                    maxProperties = PositiveInt(1)
            )
        })
    }

    @Test
    @DisplayName("minProperties cannot be greater than the number of defined properties if additionalProperties are not allowed")
    internal fun test3() {
        assertThrows(IllegalArgumentException::class.java, {
            ObjectJsonSchemaNode(
                    additionalProperties = false,
                    minProperties = PositiveInt(1)
            )
        })
    }
}
