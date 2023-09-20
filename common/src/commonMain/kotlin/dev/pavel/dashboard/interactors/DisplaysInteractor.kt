package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardRepository
import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.entity.DisplayWithDashboard
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DisplaysInteractor(
    private val displayRepository: DisplayRepository,
    private val dashboardRepository: DashboardRepository<Entities.WebPagesDashboard>,
    private val backgroundDispatcher: CoroutineDispatcher
) : DataItemsInteractor<DisplayWithDashboard> {
    override suspend fun allDataItems(): List<Pair<Entities.Display, Entities.WebPagesDashboard?>> {
        return withContext(backgroundDispatcher) {
            val displays = displayRepository.allDisplays()
            val resultList = mutableListOf<Pair<Entities.Display, Entities.WebPagesDashboard?>>()
            displays.forEach { display ->
                val boundDashboard = displayRepository.dashboardIdForDisplay(display.id())?.let {
                    dashboardRepository.findDashboardById(it)
                }
                resultList += display to boundDashboard
            }
            resultList
        }
    }

}