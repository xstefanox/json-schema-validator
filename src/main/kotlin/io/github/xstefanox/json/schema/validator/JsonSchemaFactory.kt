package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.xstefanox.json.schema.validator.JsonSchema.Companion.DEFAULT_SCHEMA_URI
import io.github.xstefanox.json.schema.validator.JsonSchema.Companion.SCHEMA_FIELD
import io.github.xstefanox.json.schema.validator.JsonSchema.Companion.SCHEMA_POINTER
import io.github.xstefanox.json.schema.validator.node.JsonSchemaNode
import java.net.URI

class JsonSchemaFactory {

    private val objectMapper: ObjectMapper by lazy {

        val simpleModule = SimpleModule()
                .addDeserializer(JsonSchemaNode::class.java, JsonSchemaNodeDeserializer())

        ObjectMapper()
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .registerModule(KotlinModule())
                .registerModule(simpleModule)
    }

    fun from(schemaString: String): JsonSchema {

        val schemaTree = objectMapper.readValue<JsonNode>(schemaString)
        val schemaNamespaceNode = schemaTree.at(SCHEMA_POINTER)

        val schemaNamespace = if (schemaNamespaceNode is TextNode) {
            URI.create(schemaNamespaceNode.asText())
        } else {
            DEFAULT_SCHEMA_URI
        }

        if (schemaTree is ObjectNode) {
            schemaTree.remove(SCHEMA_FIELD)
        }

        val rootJsonSchemaNode = objectMapper.convertValue<JsonSchemaNode>(schemaTree)

        return JsonSchema(rootJsonSchemaNode, schemaNamespace)
    }
}
