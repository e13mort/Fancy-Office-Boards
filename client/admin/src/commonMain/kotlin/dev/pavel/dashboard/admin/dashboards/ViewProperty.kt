package dev.pavel.dashboard.admin.dashboards

import kotlinx.coroutines.flow.StateFlow

interface ViewProperty<T> {
    val flow: StateFlow<T>
    var value: T
}