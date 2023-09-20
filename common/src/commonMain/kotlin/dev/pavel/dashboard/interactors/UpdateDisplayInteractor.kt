package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DisplayWithDashboard

interface UpdateDisplayInteractor {
    suspend fun createOrUpdateDisplay(
        id: String?,
        name: String,
        description: String,
        dashboardId: DashboardId?
    ): DisplayWithDashboard
}