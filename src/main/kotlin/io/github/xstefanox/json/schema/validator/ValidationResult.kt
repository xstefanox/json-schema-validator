package io.github.xstefanox.json.schema.validator

class ValidationResult(private val errors: List<ValidationError>) : List<ValidationError> by errors {

    val isSuccessful: Boolean
        get() = errors.isEmpty()
}
