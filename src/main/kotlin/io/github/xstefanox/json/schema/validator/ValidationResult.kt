package io.github.xstefanox.json.schema.validator

class ValidationResult(val errors: List<String>) {

    val isSuccessful: Boolean
            get() = errors.isEmpty()
}
