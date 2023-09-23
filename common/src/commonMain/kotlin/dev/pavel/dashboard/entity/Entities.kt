/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

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
    suspend fun delete(id: DashboardId)

    class MissedDashboardException : Throwable()
}

interface WebPagesDashboardRepository : DashboardRepository<Entities.WebPagesDashboard> {
    suspend fun updateDashboard(
        id: DashboardId,
        targets: List<String>,
        name: String,
        switchTimeoutSeconds: Int
    )

    suspend fun createDashboard(targets: List<String>, name: String, switchTimeSeconds: Int): DashboardId
}

interface DisplayRepository {
    suspend fun updateDisplay(displayId: DisplayId, name: String, description: String, dashboardId: DashboardId?)

    suspend fun createDisplay(name: String, description: String, dashboardId: DashboardId?): DisplayId

    suspend fun allDisplays(): List<Entities.Display>
    suspend fun delete(id: DisplayId)
}
