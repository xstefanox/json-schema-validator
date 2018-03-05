package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonPointer

data class ValidationError(val property: JsonPointer, val message: String)
