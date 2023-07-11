package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.Entities

interface UpdateDashboardInteractor {
    suspend fun updateDashboard(id: String, targets: List<String>, name: String): Entities.WebPagesDashboard
}