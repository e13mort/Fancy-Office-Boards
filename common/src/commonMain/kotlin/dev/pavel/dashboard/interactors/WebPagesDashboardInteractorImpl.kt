package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardRepository
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

class WebPagesDashboardInteractorImpl(
    private val repository: DashboardRepository<Entities.WebPagesDashboard>,
    private val backgroundDispatcher: CoroutineDispatcher
) : WebPagesDashboardInteractor {
    override suspend fun allDashboards(): List<Entities.WebPagesDashboard> {
        return withContext(CoroutineScope(backgroundDispatcher).coroutineContext) {
            repository.allDashboards()
        }
    }

}