package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.Entities

interface WebPagesDashboardInteractor {
    suspend fun allDashboards(): List<Entities.WebPagesDashboard>
}