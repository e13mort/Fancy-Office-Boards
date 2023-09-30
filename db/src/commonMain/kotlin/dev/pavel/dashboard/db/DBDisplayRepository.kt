/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.db

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DataDisplay
import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.DisplayRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DBDisplayRepository(
    private val queries: DisplayQueries
) : DisplayRepository {
    private val creationMutex = Mutex()
    override suspend fun updateDisplay(
        displayId: DisplayId,
        name: String,
        description: String,
        dashboardId: DashboardId?
    ) {
        queries.update(
            name,
            description,
            dashboardId?.toLong(),
            displayId.toLong()
        )
    }

    override suspend fun createDisplay(
        name: String,
        description: String,
        dashboardId: DashboardId?
    ): DisplayId {
        return creationMutex.withLock {
            queries.transactionWithResult {
                queries.insert(name, description, dashboardId?.toLong())
                queries.lastInsertRowId().executeAsList().last().toInt()
            }
        }
    }

    override suspend fun allDisplays(): List<DataDisplay> {
        return queries.selectAll().executeAsList().map {
            wrapItem(it)
        }
    }

    private fun wrapItem(it: DBDisplay) = DataDisplay(
        it.id.toInt(),
        it.name,
        it.description,
        it.dashboard_id?.toInt()
    )

    override suspend fun delete(id: DisplayId) {
        queries.delete(id.toLong())
    }

}