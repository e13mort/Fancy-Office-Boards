package dev.pavel.dashboard.admin.properties.validation

object PositiveIntValidator : Validator<String> {
    override fun validate(item: String) {
        val value = item.toIntOrNull()
        if (value == null || value < 1) {
            throw Validator.ValidationException("Value must be positive int number")
        }
    }

}