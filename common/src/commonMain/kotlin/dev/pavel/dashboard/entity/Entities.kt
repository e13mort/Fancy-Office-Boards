package dev.pavel.dashboard.entity

import kotlinx.coroutines.flow.Flow

typealias DisplayId = String
typealias DashboardId = String

interface Entities {

    sealed interface Dashboard {
        fun name(): String

        fun id(): DashboardId
    }

    interface WebPagesDashboard : Dashboard {

        fun targets(): List<String>

        fun switchTimeoutSeconds(): Int
    }

    sealed interface Display {
        fun name(): String
        fun id(): DisplayId
        fun description(): String
        fun dashboardId(): DashboardId?
    }
}

interface DashboardRepository<T : Entities.Dashboard> {
    fun observeDashboardById(id: DashboardId): Flow<T>

    suspend fun findDashboardById(id: DashboardId): T?

    suspend fun allDashboards(): List<T>

    class MissedDashboardException : Throwable()
}

interface WebPagesDashboardRepository : DashboardRepository<Entities.WebPagesDashboard> {
    suspend fun updateDashboard(id: DashboardId, targets: List<String>, name: String)

    suspend fun createDashboard(targets: List<String>, name: String): DashboardId
}

interface DisplayRepository {
    suspend fun updateDisplay(displayId: DisplayId, name: String, description: String, dashboardId: DashboardId?)

    suspend fun createDisplay(name: String, description: String, dashboardId: DashboardId?): DisplayId

    suspend fun allDisplays(): List<Entities.Display>
}
