package dev.pavel.dashboard.admin.properties

import kotlinx.coroutines.flow.StateFlow

interface ViewProperty<T> {
    val flow: StateFlow<T>
    var value: T
}