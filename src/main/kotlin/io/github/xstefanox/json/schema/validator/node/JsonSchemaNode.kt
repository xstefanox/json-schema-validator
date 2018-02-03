package io.github.xstefanox.json.schema.validator.node

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

@JsonTypeInfo(use = NAME, property = "type")
@JsonSubTypes(value = [
    Type(value = IntegerJsonSchemaNode::class, name = "integer"),
    Type(value = NumberJsonSchemaNode::class, name = "number"),
    Type(value = StringJsonSchemaNode::class, name = "string"),
    Type(value = BooleanJsonSchemaNode::class, name = "boolean"),
    Type(value = NullJsonSchemaNode::class, name = "null"),
    Type(value = ObjectJsonSchemaNode::class, name = "object"),
    Type(value = ArrayJsonSchemaNode::class, name = "array")
])
abstract class JsonSchemaNode