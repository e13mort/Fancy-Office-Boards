package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.entity.Entities

class MemoryDisplayRepository(
    private val data: MutableMap<DisplayId, DataDisplay> = mutableMapOf(
        "id0" to DataDisplay("id0","Display1",  "Memory Display 1"),
        "id1" to DataDisplay("id1","Display2",  "Memory Display 2")
    )
) : DisplayRepository {

    private val mapping = mutableMapOf<DisplayId, DashboardId>()
    override fun dashboardIdForDisplay(id: DisplayId): DashboardId? {
        return mapping[id] //todo: move to a dedicated repository
    }

    override fun saveDashboardForDisplay(displayId: DisplayId, dashboardId: DashboardId) {
        mapping[displayId] = dashboardId //todo: move to a dedicated repository
    }

    override fun updateDisplay(displayId: DisplayId, name: String, description: String) {
        val item = data[displayId] ?: throw IllegalArgumentException("Display with id $displayId doesn't exists")
        data[displayId] = item.copy(name = name, description = description)
    }

    override fun createDisplay(name: String, description: String): DisplayId {
        val newId = "id${data.size}"
        data[newId] = DataDisplay(newId, name, description)
        return newId
    }

    override fun allDisplays(): List<Entities.Display> {
        return data.values.toList()
    }
}