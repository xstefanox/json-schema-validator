package io.github.xstefanox.json.schema.validator.node

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

@JsonTypeInfo(use = NAME, property = "type")
@JsonSubTypes(value = [
    Type(value = IntegerJsonSchemaNode::class, name = "integer")
])
@JsonIgnoreProperties(value = ["\$schema"])
abstract class JsonSchemaNode