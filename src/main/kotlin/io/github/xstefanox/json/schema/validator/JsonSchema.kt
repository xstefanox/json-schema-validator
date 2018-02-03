package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonPointer
import io.github.xstefanox.json.schema.validator.node.JsonSchemaNode
import java.net.URI

class JsonSchema(private val root: JsonSchemaNode, val schema: URI) {

    companion object {

        @JvmField
        val SCHEMA_POINTER: JsonPointer = JsonPointer.compile("/\$schema")

        const val SCHEMA_FIELD = "\$schema"

        @JvmField
        val DEFAULT_SCHEMA_URI = URI.create("http://json-schema.org/schema#")!!
    }
}