package io.github.xstefanox.json.schema.validator

class ValidationResult(val errors: List<ValidationError>) {

    val isSuccessful: Boolean
            get() = errors.isEmpty()
}
