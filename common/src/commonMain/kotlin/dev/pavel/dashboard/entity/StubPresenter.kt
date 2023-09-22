package dev.pavel.dashboard.entity

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StubPresenter(
    private val displayRepository: DisplayRepository,
    private val dashboardRepository: DashboardRepository<Entities.WebPagesDashboard>,
    dispatcher: CoroutineDispatcher
) : Presenter {

    private val _states: MutableStateFlow<Presenter.UIState> = MutableStateFlow(Presenter.UIState.Loading)
    private val coroutineScope = CoroutineScope(dispatcher)
    private var activeJob: Job? = null
    fun start() {
        coroutineScope.launch {
            val displays = displayRepository.allDisplays()
            _states.value = Presenter.UIState.Displays(displays)
        }
    }
    override fun observeUIStates(): StateFlow<Presenter.UIState> {
        return _states
    }

    fun onDisplaySelected(id: DashboardId?) {
        if (id == null) return
        coroutineScope.launch {
            _states.value = Presenter.UIState.Loading
            val dashboardById = dashboardRepository.findDashboardById(id) ?: return@launch
            val links = dashboardById.targets().toMutableList()
            activeJob?.cancel()
            activeJob = launch {
                while (currentCoroutineContext().isActive) {
                    val first = links.removeFirst()
                    _states.value = Presenter.UIState.WebPage(first)
                    links.add(first)
                    delay(dashboardById.switchTimeoutSeconds() * 1000L)
                }
            }
        }
    }

}