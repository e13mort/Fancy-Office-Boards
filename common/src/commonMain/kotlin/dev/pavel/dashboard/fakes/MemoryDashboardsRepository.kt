package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.entity.WebPagesDashboardRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class MemoryDashboardsRepository(
    private val dashboards: MutableMap<DashboardId, Entities.WebPagesDashboard>,
    private val delay: Long = 0
) : WebPagesDashboardRepository {
    override fun observeDashboardById(id: DashboardId): Flow<Entities.WebPagesDashboard> {
        return emptyFlow()
    }

    override suspend fun findDashboardById(id: DashboardId): Entities.WebPagesDashboard? {
        return dashboards[id]
    }

    override suspend fun allDashboards(): List<Entities.WebPagesDashboard> {
        delay(delay)
        return dashboards.values.toList()
    }

    override suspend fun updateDashboard(id: DashboardId, targets: List<String>, name: String) {
        delay(delay)
        dashboards[id] = SimpleWebPagesDashboard(name, targets, 0, id)
    }

    override suspend fun createDashboard(targets: List<String>, name: String): DashboardId {
        return "id${dashboards.size + 1}".also {
            dashboards[it] = SimpleWebPagesDashboard(name, targets, 0, it)
        }
    }

    companion object {
        fun create() = MemoryDashboardsRepository(
            mutableMapOf(
                "id1" to FakeWebDashboard(
                    listOf("https://android.com", "https://google.com", "https://jetbrains.com"), 10, "Dashboard 1", "id1"
                ),
                "id2" to FakeWebDashboard(
                    listOf("https://android.com", "https://google.com", "https://jetbrains.com"), 10, "Dashboard 2", "id2"
                )
            ),
            delay = 500
        )
    }

    private data class SimpleWebPagesDashboard(
        val _name: String,
        val _targets: List<String>,
        val _timeOut: Int,
        val _id: DashboardId,
    ) : Entities.WebPagesDashboard {

        override fun id(): DashboardId = _id

        override fun targets(): List<String> = _targets

        override fun switchTimeoutSeconds(): Int = _timeOut

        override fun name(): String = _name
    }
}