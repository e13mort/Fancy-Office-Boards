package dev.pavel.dashboard.admin

import dev.pavel.dashboard.entity.DashboardRepository
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel
import kotlin.coroutines.CoroutineContext

class DashboardsPM(
    params: PmParams,
    private val repository: DashboardRepository,
    private val networkContext: CoroutineContext
) : PresentationModel(params) {
    @Serializable
    object Description : PmDescription


    sealed interface State

    object LOADING : State
    data class ItemsState(
        val dashboards: List<Dashboard> = emptyList()
    ) : State

    data class Dashboard(
        val name: String,
        val links: List<String>
    )

    private val _flow = MutableStateFlow<State>(LOADING)

    suspend fun load() {
        _flow.value = LOADING
        val items = withContext(networkContext) {
            async { repository.allDashboards() }
        }.await()
        val stateItems = items.map {
            when (it) {
                is Entities.WebPagesDashboard -> Dashboard(it.name(), it.targets())
            }
        }
        _flow.value = ItemsState(dashboards = stateItems)
    }

    fun dashboards(): StateFlow<State> {
        return _flow
    }

    fun addNewDashboard() {
        when(val value = _flow.value) {
            is ItemsState -> {
                val dashboards = value.dashboards
                _flow.value = value.copy(
                    dashboards = dashboards.toMutableList().also {
                        it.add(Dashboard("Dashboard ${dashboards.size + 1}", emptyList()))
                    }
                )
            }
            LOADING -> Unit
        }
    }
}