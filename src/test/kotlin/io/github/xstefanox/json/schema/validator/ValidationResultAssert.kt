package io.github.xstefanox.json.schema.validator

import org.assertj.core.api.AbstractListAssert


class ValidationResultAssert(
        validationResult: ValidationResult
) : AbstractListAssert<ValidationResultAssert, ValidationResult, ValidationError, ValidationErrorAssert>(
        validationResult,
        ValidationResultAssert::class.java
) {

    override fun toAssert(value: ValidationError?, description: String?): ValidationErrorAssert {

        if (value == null) {
            throw IllegalArgumentException("value must not be null")
        }

        return ValidationErrorAssert(value)
    }

    fun isSuccessful(): ValidationResultAssert {

        if (!actual.isSuccessful) {
            failWithMessage("expected validation to be successful")
        }

        return this
    }

    fun isNotSuccessful(): ValidationResultAssert {

        if (actual.isSuccessful) {
            failWithMessage("expected validation not to be successful")
        }

        return this
    }

    fun at(index: Int): ValidationErrorInResultAssert {
        return ValidationErrorInResultAssert(actual[index], actual)
    }
}