package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.WebPagesDashboardInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel

class DashboardsPM(
    params: PmParams,
    private val webPagesDashboardInteractor: WebPagesDashboardInteractor
) : PresentationModel(params) {
    @Serializable
    object Description : PmDescription
    init {
        messageHandler.addHandler { msg ->
            when(msg) {
                is DashboardPM.CancelMessage -> {
                    removeNewItems()
                    true
                }
                else -> false
            }
        }
    }

    private fun removeNewItems() {
        when(val state = _flow.value) {
            is ItemsState -> {
                val dashboards = state.dashboards
                _flow.value = state.copy(
                    dashboards.toMutableList().filter { item ->
                        if (item.isNew()) {
                            detachChild(item)
                            return@filter false
                        }
                        true
                    }
                )
            }
            LOADING -> Unit
        }
    }

    sealed interface State

    object LOADING : State
    data class ItemsState(
        val dashboards: List<DashboardPM> = emptyList()
    ) : State {
        fun addButtonEnabled(): Boolean {
            return dashboards.find { dashboardPM ->
                dashboardPM.isNew()
            } == null
        }
    }

    private val _flow = MutableStateFlow<State>(LOADING)

    suspend fun load() {
        detachItems()
        _flow.value = LOADING
        val items = webPagesDashboardInteractor.allDashboards()
        _flow.value = ItemsState(dashboards = wrapAndAttachChildren(items))
    }

    private fun wrapAndAttachChildren(items: List<Entities.Dashboard>): List<DashboardPM> {
        return items.map {
            when (it) {
                is Entities.WebPagesDashboard -> {
                    Child<DashboardPM>(DashboardPM.Description(it.id(), it.name(), it.targets())).also { pm ->
                        attachChild(pm)
                    }
                }
            }
        }
    }

    private fun detachItems() {
        val value = _flow.value
        if (value is ItemsState) {
            for (dashboard in value.dashboards) {
                detachChild(dashboard)
            }
        }
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
                        val newItem = Child<DashboardPM>(DashboardPM.Description())
                        attachChild(newItem)
                        it.add(newItem)
                    }
                )
            }
            LOADING -> Unit
        }
    }
}