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

    fun hasMessage(message: String): ValidationErrorAssert {
        if (actual.message != message) {
            failWithMessage("""
                $actual
                expected message: $message
                   found message: ${actual.message}
                """.trimIndent())
        }

        return this
    }
}
