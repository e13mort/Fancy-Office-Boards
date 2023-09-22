package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateDisplayInteractorImpl(
    private val displayRepository: DisplayRepository,
    private val backgroundDispatcher: CoroutineDispatcher
) :
    UpdateDisplayInteractor {
    override suspend fun createOrUpdateDisplay(
        id: String?,
        name: String,
        description: String,
        dashboardId: DashboardId?
    ): Entities.Display {
        return withContext(backgroundDispatcher) {
            val targetDisplayId = id ?: displayRepository.createDisplay(name, description, dashboardId)
            if (targetDisplayId == id) {
                displayRepository.updateDisplay(
                    displayId = targetDisplayId, name = name, description = description, dashboardId = dashboardId
                )
            }
            displayRepository.allDisplays().first {
                it.id() == targetDisplayId
            }
        }
    }

    override suspend fun deleteDisplay(id: DisplayId) {
        withContext(backgroundDispatcher) {
            displayRepository.delete(id)
        }
    }

}