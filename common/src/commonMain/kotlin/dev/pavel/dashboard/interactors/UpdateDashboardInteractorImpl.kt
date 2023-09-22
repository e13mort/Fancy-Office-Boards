package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.entity.WebPagesDashboardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateDashboardInteractorImpl(
    private val dashboardRepository: WebPagesDashboardRepository,
    private val backgroundDispatcher: CoroutineDispatcher
) : UpdateDashboardInteractor {
    override suspend fun updateDashboard(id: DashboardId, targets: List<String>, name: String): Entities.WebPagesDashboard {
        return withContext(backgroundDispatcher) {
            dashboardRepository.updateDashboard(id, targets, name)
            val updatedDashboard = dashboardRepository.findDashboardById(id)
            updatedDashboard ?: throw IllegalStateException("Failed to load updated dashboard info")
        }
    }

    override suspend fun delete(id: DashboardId) {
        withContext(backgroundDispatcher) {
            dashboardRepository.delete(id)
        }
    }
}

