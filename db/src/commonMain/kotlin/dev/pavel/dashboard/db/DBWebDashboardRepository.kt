/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.WebPagesDashboardRepository
import dev.pavel.dashboard.fakes.DataWebDashboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DBWebDashboardRepository(
    private val dashboardsQueries: DashboardQueries
) : WebPagesDashboardRepository {

    private val insertMutex = Mutex()
    override suspend fun updateDashboard(
        id: DashboardId,
        targets: List<String>,
        name: String,
        switchTimeoutSeconds: Int
    ) {
        dashboardsQueries.update(
            name,
            switchTimeoutSeconds.toLong(),
            encodeTargets(targets),
            id.toLong()
        )
    }

    override suspend fun createDashboard(
        targets: List<String>,
        name: String,
        switchTimeSeconds: Int
    ): DashboardId {
        return insertMutex.withLock {
            dashboardsQueries.transactionWithResult {
                dashboardsQueries.insert(
                    name = name,
                    switchTimeoutSeconds = switchTimeSeconds.toLong(),
                    targets = encodeTargets(targets)
                )
                dashboardsQueries.lastInsertRowId().executeAsList().last().toInt()
            }
        }
    }

    override fun observeDashboardById(id: DashboardId): Flow<DataWebDashboard> {
        return dashboardsQueries.find(id.toLong()).asFlow()
            .mapToOne(Dispatchers.IO)
            .map {
                wrapItem(it)
            }
    }

    override suspend fun findDashboardById(id: DashboardId): DataWebDashboard? {
        return dashboardsQueries.find(id.toLong()).executeAsOneOrNull()?.let {
            wrapItem(it)
        }
    }

    override suspend fun allDashboards(): List<DataWebDashboard> {
        return dashboardsQueries.selectAll().executeAsList().map {
            wrapItem(it)
        }
    }

    override suspend fun delete(id: DashboardId) {
        dashboardsQueries.delete(id.toLong())
    }

    private fun wrapItem(it: DBWebDashboard): DataWebDashboard {
        return DataWebDashboard(
            decodeTargets(it.targets),
            it.switchTimeoutSeconds.toInt(),
            it.name,
            it.id.toInt()
        )
    }

    private fun encodeTargets(targets: List<String>): String {
        return Json.encodeToString(targets)
    }

    private fun decodeTargets(raw: String): List<String> {
        return try {
            Json.decodeFromString(raw)
        } catch (e: Exception) {
            emptyList()
        }
    }
}