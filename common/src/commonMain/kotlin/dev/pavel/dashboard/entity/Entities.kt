package dev.pavel.dashboard.entity

import kotlinx.coroutines.flow.Flow

typealias DisplayId = String
typealias DashboardId = String

interface Entities {

    sealed interface Dashboard {
        fun name(): String
    }

    interface WebPagesDashboard : Dashboard {

        fun targets(): List<String>

        fun switchTimeoutSeconds(): Int
    }

    interface DashboardDisplay {
        fun activeDashboard(): Dashboard
    }
}

interface DashboardRepository {
    fun dashboardById(id: DashboardId): Flow<Entities.Dashboard>

    suspend fun allDashboards(): List<Entities.Dashboard>

    class MissedDashboardException : Throwable()
}

interface DisplayRepository {
    fun dashBoardIdForDisplay(id: DisplayId): DashboardId?
}
