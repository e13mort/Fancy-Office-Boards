package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DashboardRepository
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeDashboardsRepository(
    private val dashboards: List<Entities.Dashboard>,
    private val delay: Long = 0
) : DashboardRepository {
    override fun dashboardById(id: DashboardId): Flow<Entities.Dashboard> {
        return emptyFlow()
    }

    override suspend fun allDashboards(): List<Entities.Dashboard> {
        delay(delay)
        return dashboards
    }

    companion object {
        fun create() = FakeDashboardsRepository(
            listOf(
                FakeWebDashboard(
                    listOf("https://android.com", "https://google.com", "https://jetbrains.com"), 10, "Dashboard 1"
                ),
                FakeWebDashboard(
                    listOf("https://android.com", "https://google.com", "https://jetbrains.com"), 10, "Dashboard 2"
                )
            ),
            delay = 500
        )

    }
}