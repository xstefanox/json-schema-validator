package io.github.xstefanox.json.schema.validator

import kotlin.reflect.KClass

/**
 * Thrown when an unexpected class is encountered whn serializing a [JsonSchema].
 */
class UnsupportedJsonSchemaClassException(clazz: KClass<out Any>) : RuntimeException("unsupported JSON Schema class: $clazz")
