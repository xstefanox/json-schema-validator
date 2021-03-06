package io.github.xstefanox.json.schema.validator

fun assertThat(validationError: ValidationError): ValidationErrorAssert {
    return ValidationErrorAssert(validationError)
}

fun assertThat(validationResult: ValidationResult): ValidationResultAssert {
    return ValidationResultAssert(validationResult)
}
