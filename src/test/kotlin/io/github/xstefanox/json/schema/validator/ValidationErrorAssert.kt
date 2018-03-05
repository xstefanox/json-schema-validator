package io.github.xstefanox.json.schema.validator

import com.fasterxml.jackson.core.JsonPointer
import org.assertj.core.api.AbstractAssert

class ValidationErrorAssert(validationError: ValidationError) : AbstractAssert<ValidationErrorAssert, ValidationError>(validationError, ValidationErrorAssert::class.java) {

    fun pointsTo(jsonPointer: String): ValidationErrorAssert {

        val expected = JsonPointer.compile(jsonPointer)

        if (actual.property != expected) {
            failWithMessage("expected $actual to point to $expected")
        }

        return this
    }

    fun hasMessage(): ValidationErrorAssert {
        if (actual.message.isBlank()) {
            failWithMessage("expected $actual to have a validation message")
        }

        return this
    }
}
