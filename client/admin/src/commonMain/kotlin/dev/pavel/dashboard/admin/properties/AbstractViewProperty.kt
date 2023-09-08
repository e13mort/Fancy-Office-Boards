package dev.pavel.dashboard.admin.properties

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class AbstractViewProperty<T>(initialValue: T) : ViewProperty<T> {
    private val _property = MutableStateFlow(initialValue)
    override val flow: StateFlow<T>
        get() = _property

    override var value: T
        get() = _property.value
        set(value) = update(value)

    override fun toString(): String {
        return value.toString()
    }

    private fun update(newValue: T) {
        // filter property here
        _property.value = newValue
    }
}