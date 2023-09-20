package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DashboardRepository
import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.entity.DisplayWithDashboard
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateDisplayInteractorImpl(
    private val displayRepository: DisplayRepository,
    private val dashboardRepository: DashboardRepository<out Entities.Dashboard>,
    private val backgroundDispatcher: CoroutineDispatcher
) :
    UpdateDisplayInteractor {
    override suspend fun createOrUpdateDisplay(
        id: String?,
        name: String,
        description: String,
        dashboardId: DashboardId?
    ): DisplayWithDashboard {
        return withContext(backgroundDispatcher) {
            val targetDashboardId = id ?: displayRepository.createDisplay(name, description)
            if (targetDashboardId == id) {
                displayRepository.updateDisplay(
                    displayId = targetDashboardId, name = name, description = description
                )
            }
            if (dashboardId != null) {
                displayRepository.saveDashboardForDisplay(targetDashboardId, dashboardId)
            }
            val updatedDisplay = displayRepository.allDisplays().first {
                it.id() == targetDashboardId
            }
            val dashboardIdForDisplay = displayRepository.dashboardIdForDisplay(targetDashboardId)
            val dashboard = if (dashboardIdForDisplay != null) {
                dashboardRepository.findDashboardById(dashboardIdForDisplay)
            } else null
            updatedDisplay to dashboard
        }
    }

}