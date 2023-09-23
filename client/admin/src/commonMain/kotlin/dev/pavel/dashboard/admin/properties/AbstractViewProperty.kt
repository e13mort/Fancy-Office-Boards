package dev.pavel.dashboard.admin.properties

import dev.pavel.dashboard.admin.properties.validation.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class AbstractViewProperty<T>(
    initialValue: T,
    internal val validators: List<Validator<T>>
) : ViewProperty<T> {
    private val _property = MutableStateFlow(initialValue)
    private val _errors = MutableStateFlow<String?>(null)
    override val flow: StateFlow<T>
        get() = _property

    override var value: T
        get() = _property.value
        set(value) = update(value)
    override val errors: StateFlow<String?>
        get() = _errors

    override fun toString(): String {
        return value.toString()
    }

    private fun update(newValue: T) {
        _errors.value = null
        try {
            validators.forEach {
                it.validate(newValue)
            }
        } catch (exc: Validator.ValidationException) {
            _errors.value = exc.message
        }
        _property.value = newValue
    }
}