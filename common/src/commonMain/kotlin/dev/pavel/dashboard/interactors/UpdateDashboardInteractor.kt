package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities

interface UpdateDashboardInteractor {
    suspend fun updateDashboard(id: String, targets: List<String>, name: String, switchDelaySeconds: Int): Entities.WebPagesDashboard
    suspend fun delete(id: DashboardId)
}