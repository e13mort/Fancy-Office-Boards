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

    interface Display {
        fun name(): String
        fun id(): DisplayId
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
    fun dashboardIdForDisplay(id: DisplayId): DashboardId?

    fun saveDashboardForDisplay(displayId: DisplayId, dashboardId: DashboardId)

    fun updateDisplayName(displayId: DisplayId, name: String)

    fun createDisplay(name: String, dashboardId: DashboardId): DisplayId

    fun allDisplays(): List<Entities.Display>
}
