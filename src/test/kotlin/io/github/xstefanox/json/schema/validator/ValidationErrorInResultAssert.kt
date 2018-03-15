package io.github.xstefanox.json.schema.validator


class ValidationErrorInResultAssert(
        validationError: ValidationError,
        val validationResult: ValidationResult
) : ValidationErrorAssert(validationError) {

    fun at(index: Int): ValidationErrorInResultAssert {
        return ValidationErrorInResultAssert(validationResult[index], validationResult)
    }

    override fun pointsTo(jsonPointer: String): ValidationErrorInResultAssert {
        super.pointsTo(jsonPointer)
        return this
    }

    override fun hasMessage(message: String): ValidationErrorInResultAssert {
        super.hasMessage(message)
        return this
    }
}
