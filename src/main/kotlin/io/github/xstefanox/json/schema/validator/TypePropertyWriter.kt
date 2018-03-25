package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.AnnotatedClass
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition
import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter
import com.fasterxml.jackson.databind.util.Annotations
import io.github.xstefanox.json.schema.validator.node.ArrayJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.BooleanJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.IntegerJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NullJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.NumberJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.ObjectJsonSchemaNode
import io.github.xstefanox.json.schema.validator.node.StringJsonSchemaNode

class TypePropertyWriter : VirtualBeanPropertyWriter {

    // used by Jackson to instantiate, reading type from @JsonAppend.Prop
    @Suppress("unused")
    constructor() : super()

    constructor(propDef: BeanPropertyDefinition?, contextAnnotations: Annotations?, declaredType: JavaType?) : super(propDef, contextAnnotations, declaredType)

    override fun value(bean: Any?, gen: JsonGenerator?, prov: SerializerProvider?): Any {
        return when (bean) {
            is BooleanJsonSchemaNode -> "boolean"
            is StringJsonSchemaNode -> "string"
            is NullJsonSchemaNode -> "null"
            is IntegerJsonSchemaNode -> "integer"
            is NumberJsonSchemaNode -> "number"
            is ArrayJsonSchemaNode -> "array"
            is ObjectJsonSchemaNode -> "object"
            else -> throw UnsupportedJsonSchemaClassException(bean!!::class)
        }
    }

    override fun withConfig(config: MapperConfig<*>?, declaringClass: AnnotatedClass?, propDef: BeanPropertyDefinition?, type: JavaType?): VirtualBeanPropertyWriter {
        return TypePropertyWriter(propDef, declaringClass!!.annotations, type)
    }
}