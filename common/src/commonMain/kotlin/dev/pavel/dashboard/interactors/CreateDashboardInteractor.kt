package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.Entities

interface CreateDashboardInteractor {
    suspend fun createDashboard(targets: List<String>, name: String): Entities.WebPagesDashboard
}