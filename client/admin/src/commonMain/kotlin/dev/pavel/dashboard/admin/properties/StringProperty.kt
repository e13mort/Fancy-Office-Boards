package dev.pavel.dashboard.admin.properties

import dev.pavel.dashboard.admin.properties.validation.Validator

class StringProperty(initialValue: String, validators: List<Validator<String>> = emptyList()) :
    AbstractViewProperty<String>(initialValue, validators)

fun StringProperty.copy() : StringProperty {
    // don't copy errors
    return StringProperty(
        initialValue = value,
        validators = validators
    )
}