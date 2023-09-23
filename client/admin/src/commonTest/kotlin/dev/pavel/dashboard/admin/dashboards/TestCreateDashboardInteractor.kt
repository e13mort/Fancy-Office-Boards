package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.CreateDashboardInteractor

internal class TestCreateDashboardInteractor : CreateDashboardInteractor {
    override suspend fun createDashboard(
        targets: List<String>, name: String, switchDelaySeconds: Int
    ): Entities.WebPagesDashboard {
        TODO("Not yet implemented")
    }
}