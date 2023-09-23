/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DataDisplay
import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.DisplayRepository

class MemoryDisplayRepository(
    private val data: MutableMap<DisplayId, DataDisplay> = mutableMapOf(
        0 to DataDisplay(0,"Display1",  "Memory Display 1", null),
        1 to DataDisplay(1,"Display2",  "Memory Display 2", null)
    )
) : DisplayRepository {

    override suspend fun updateDisplay(
        displayId: DisplayId,
        name: String,
        description: String,
        dashboardId: DashboardId?
    ) {
        val item = data[displayId] ?: throw IllegalArgumentException("Display with id $displayId doesn't exists")
        data[displayId] = item.copy(name = name, description = description, dashboardId = dashboardId)
    }

    override suspend fun createDisplay(name: String, description: String, dashboardId: DashboardId?): DisplayId {
        val newId = data.size
        data[newId] = DataDisplay(newId, name, description, dashboardId)
        return newId
    }

    override suspend fun allDisplays(): List<DataDisplay> {
        return data.values.toList()
    }

    override suspend fun delete(id: DisplayId) {
        data.remove(id)
    }
}