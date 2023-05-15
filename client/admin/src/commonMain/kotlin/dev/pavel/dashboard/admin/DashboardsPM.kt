package dev.pavel.dashboard.admin

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel

class DashboardsPM(params: PmParams) : PresentationModel(params) {
    @Serializable
    object Description : PmDescription

    data class State(
        val dashboards: List<Dashboard>
    )

    data class Dashboard(
        val name: String
    )

    private val _flow = MutableStateFlow(
        State(
            listOf(
                Dashboard("Dashboard 1"), Dashboard("Dashboard 2")
            )
        )
    )

    fun dashboards(): StateFlow<State> {
        return _flow
    }

    fun addNewDashboard() {
        val value = _flow.value
        val dashboards = value.dashboards
        _flow.value = value.copy(
            dashboards = dashboards.toMutableList().also {
                it.add(Dashboard("Dashboard ${dashboards.size + 1}"))
            }
        )
    }
}