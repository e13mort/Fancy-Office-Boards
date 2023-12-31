/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

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

    override suspend fun updateDashboard(
        id: DashboardId,
        targets: List<String>,
        name: String,
        switchTimeoutSeconds: Int
    ) {
        delay(delay)
        dashboards[id] = DataWebDashboard(targets, switchTimeoutSeconds, name, id)
    }

    override suspend fun createDashboard(
        targets: List<String>,
        name: String,
        switchTimeSeconds: Int
    ): DashboardId {
        return dashboards.size + 1.also {
            dashboards[it] = DataWebDashboard(targets, switchTimeSeconds, name, it)
        }
    }

    companion object {
        fun create(delay: Long = 500) = MemoryDashboardsRepository(
            mutableMapOf(
                1 to DataWebDashboard(
                    listOf("https://android.com", "https://google.com", "https://jetbrains.com"), 10, "Dashboard 1", 1
                ),
                2 to DataWebDashboard(
                    listOf("https://android.com", "https://google.com", "https://jetbrains.com"), 10, "Dashboard 2", 2
                )
            ),
            delay = delay
        )
    }
}