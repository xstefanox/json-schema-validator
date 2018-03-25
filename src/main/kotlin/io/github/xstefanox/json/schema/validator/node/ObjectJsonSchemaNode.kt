package io.github.xstefanox.json.schema.validator.node

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonAppend
import io.github.xstefanox.json.schema.validator.TypePropertyWriter
import io.github.xstefanox.json.schema.validator.model.PositiveInt

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAppend(
        prepend = true,
        props = [
            JsonAppend.Prop(name = "type", value = TypePropertyWriter::class)
        ]
)
data class ObjectJsonSchemaNode(
        val properties: Properties? = null,
        val required: Set<Property> = emptySet(),
        val additionalProperties: Boolean = true,
        val minProperties: PositiveInt = PositiveInt(0),
        val maxProperties: PositiveInt? = null
) : JsonSchemaNode() {

    /**
     * Object properties definition.
     */
    data class Properties(
            private val properties: Map<Property, JsonSchemaNode>
    ) : Map<Property, JsonSchemaNode> by properties

    /**
     * Single property name representation.
     */
    data class Property constructor(private val name: String) {

        init {

            if (name.isEmpty()) {
                throw IllegalArgumentException("a property name cannot be empty")
            }

            if (name.isBlank()) {
                throw IllegalArgumentException("a property name cannot be blank")
            }

            if (name.trim() != name) {
                throw IllegalArgumentException("a property name cannot start or end with blank characters")
            }
        }

        @JsonValue
        override fun toString(): String {
            return name
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }

        override fun equals(other: Any?): Boolean {

            if (this === other) {
                return true
            }

            if (javaClass != other?.javaClass) {
                return false
            }

            other as Property

            if (name != other.name) {
                return false
            }

            return true
        }
    }

    init {

        if (!additionalProperties && minProperties > properties?.size ?: 0) {
            throw IllegalArgumentException("""
                minProperties cannot be greater the defined properties if additionalProperties are not allowed
                """.trimIndent())
        }

        if (maxProperties != null) {

            if (minProperties > maxProperties) {
                throw IllegalArgumentException("maxProperties must be geater or equal to minProperties")
            }

            if (maxProperties < required.size) {
                throw IllegalArgumentException("required properties cannot be greater than maxProperties")
            }
        }
    }
}
