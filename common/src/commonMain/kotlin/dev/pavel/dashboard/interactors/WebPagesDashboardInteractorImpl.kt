package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardRepository
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WebPagesDashboardInteractorImpl(
    private val repository: DashboardRepository<Entities.WebPagesDashboard>,
    private val backgroundDispatcher: CoroutineDispatcher
) : DataItemsInteractor<Entities.WebPagesDashboard> {
    override suspend fun allDataItems(): List<Entities.WebPagesDashboard> {
        return withContext(backgroundDispatcher) {
            repository.allDashboards()
        }
    }
}