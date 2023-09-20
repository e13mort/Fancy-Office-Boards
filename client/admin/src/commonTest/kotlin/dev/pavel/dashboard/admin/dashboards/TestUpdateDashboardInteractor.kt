package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.UpdateDashboardInteractor

internal class TestUpdateDashboardInteractor : UpdateDashboardInteractor {
    override suspend fun updateDashboard(
        id: String, targets: List<String>, name: String
    ): Entities.WebPagesDashboard {
        TODO("Not yet implemented")
    }

}