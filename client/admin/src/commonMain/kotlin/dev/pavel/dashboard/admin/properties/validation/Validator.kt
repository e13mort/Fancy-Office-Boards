package dev.pavel.dashboard.admin.properties.validation

interface Validator<T> {
    fun validate(item: T)

    class ValidationException(description: String) : Exception(description)
}