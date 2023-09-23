package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.entity.WebPagesDashboardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

class CreateDashboardInteractorImpl(
    private val dashboardRepository: WebPagesDashboardRepository,
    private val backgroundDispatcher: CoroutineDispatcher
) : CreateDashboardInteractor {
    override suspend fun createDashboard(
        targets: List<String>,
        name: String,
        switchDelaySeconds: Int
    ): Entities.WebPagesDashboard {
        return withContext(CoroutineScope(backgroundDispatcher).coroutineContext) {
            val id = dashboardRepository.createDashboard(targets, name, switchDelaySeconds)
            val createdDashboard = dashboardRepository.findDashboardById(id)
            createdDashboard ?: throw IllegalStateException("Failed to load created dashboard info")
        }
    }
}