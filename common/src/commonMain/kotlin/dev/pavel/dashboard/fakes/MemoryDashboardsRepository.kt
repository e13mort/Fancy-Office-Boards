package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.WebPagesDashboardRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class MemoryDashboardsRepository(
    private val dashboards: MutableMap<DashboardId, DataWebDashboard>,
    private val delay: Long = 0
) : WebPagesDashboardRepository {
    override fun observeDashboardById(id: DashboardId): Flow<DataWebDashboard> {
        return emptyFlow()
    }

    override suspend fun findDashboardById(id: DashboardId): DataWebDashboard? {
        return dashboards[id]
    }

    override suspend fun allDashboards(): List<DataWebDashboard> {
        delay(delay)
        return dashboards.values.toList()
    }

    override suspend fun delete(id: DashboardId) {
        dashboards.remove(id)
    }

    override suspend fun updateDashboard(id: DashboardId, targets: List<String>, name: String) {
        delay(delay)
        dashboards[id] = DataWebDashboard(targets, 0, name, id)
    }

    override suspend fun createDashboard(targets: List<String>, name: String): DashboardId {
        return "id${dashboards.size + 1}".also {
            dashboards[it] = DataWebDashboard(targets, 0, name, it)
        }
    }

    companion object {
        fun create(delay: Long = 500) = MemoryDashboardsRepository(
            mutableMapOf(
                "id1" to DataWebDashboard(
                    listOf("https://android.com", "https://google.com", "https://jetbrains.com"), 10, "Dashboard 1", "id1"
                ),
                "id2" to DataWebDashboard(
                    listOf("https://android.com", "https://google.com", "https://jetbrains.com"), 10, "Dashboard 2", "id2"
                )
            ),
            delay = delay
        )
    }
}