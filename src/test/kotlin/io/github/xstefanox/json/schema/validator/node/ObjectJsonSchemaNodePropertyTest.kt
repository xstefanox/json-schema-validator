package io.github.xstefanox.json.schema.validator.node

import TestUtils.Companion.OBJECT_MAPPER
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ObjectJsonSchemaNodePropertyTest {

    @Test
    @DisplayName("a property should be serialized as its name")
    internal fun test1() {
        assertThat(OBJECT_MAPPER.writeValueAsString(ObjectJsonSchemaNode.Property("property1"))).isEqualTo("""
            "property1"
        """.trimIndent())
    }

    @Test
    @DisplayName("a property should be deserialized from a string")
    internal fun test2() {

        val deserializedProperty = OBJECT_MAPPER.readValue<ObjectJsonSchemaNode.Property>("""
            "my-property"
        """.trimIndent())

        assertThat(deserializedProperty).isEqualTo(ObjectJsonSchemaNode.Property("my-property"))
    }

    @Test
    @DisplayName("properties having the same name should be equal")
    internal fun test3() {

        val property1 = ObjectJsonSchemaNode.Property("property1")
        val property2 = ObjectJsonSchemaNode.Property("property1")

        assertThat(property1).isEqualTo(property2)
    }

    @Test
    @DisplayName("the string representation of a property should be the property name")
    internal fun test4() {

        val property = ObjectJsonSchemaNode.Property("property1")

        assertThat(property.toString()).isEqualTo("property1")
    }

    @Test
    @DisplayName("properties cannot have an empty name")
    internal fun test5() {
        assertThrows(IllegalArgumentException::class.java, { ObjectJsonSchemaNode.Property("") })
    }

    @Test
    @DisplayName("properties cannot have a blank name")
    internal fun test6() {
        assertThrows(IllegalArgumentException::class.java, { ObjectJsonSchemaNode.Property("   ") })
    }

    @Test
    @DisplayName("property name cannot start or end with blank characters")
    internal fun test7() {
        assertThrows(IllegalArgumentException::class.java, { ObjectJsonSchemaNode.Property(" my-property ") })
    }

    @Test
    @DisplayName("property name can contain spaces")
    internal fun test8() {
        ObjectJsonSchemaNode.Property("my property")
    }

    @Test
    @DisplayName("properties having different name should be different")
    internal fun test9() {

        val property1 = ObjectJsonSchemaNode.Property("property1")
        val property2 = ObjectJsonSchemaNode.Property("property2")

        assertThat(property1).isNotEqualTo(property2)
    }
}
