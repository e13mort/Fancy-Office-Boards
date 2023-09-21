package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DataDisplay
import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.DisplayRepository

class MemoryDisplayRepository(
    private val data: MutableMap<DisplayId, DataDisplay> = mutableMapOf(
        "id0" to DataDisplay("id0","Display1",  "Memory Display 1", null),
        "id1" to DataDisplay("id1","Display2",  "Memory Display 2", null)
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
        val newId = "id${data.size}"
        data[newId] = DataDisplay(newId, name, description, dashboardId)
        return newId
    }

    override suspend fun allDisplays(): List<DataDisplay> {
        return data.values.toList()
    }
}