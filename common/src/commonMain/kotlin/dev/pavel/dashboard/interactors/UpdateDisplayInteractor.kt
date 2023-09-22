package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.Entities

interface UpdateDisplayInteractor {
    suspend fun createOrUpdateDisplay(
        id: String?,
        name: String,
        description: String,
        dashboardId: DashboardId?
    ): Entities.Display

    suspend fun deleteDisplay(id: DisplayId)
}