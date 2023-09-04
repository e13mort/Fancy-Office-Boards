package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.entity.Entities

class MemoryDisplayRepository(
    private val data: MutableMap<DisplayId, DataDisplay> = mutableMapOf(
        "id1" to DataDisplay("Display1", "id1"),
        "id2" to DataDisplay("Display2", "id2")
    )
) : DisplayRepository {

    private val mapping = mutableMapOf<DisplayId, DashboardId>()
    override fun dashboardIdForDisplay(id: DisplayId): DashboardId? {
        return mapping[id] //todo: move to a dedicated repository
    }

    override fun saveDashboardForDisplay(displayId: DisplayId, dashboardId: DashboardId) {
        mapping[displayId] = dashboardId //todo: move to a dedicated repository
    }

    override fun updateDisplayName(displayId: DisplayId, name: String) {
        val item = data[displayId] ?: throw IllegalArgumentException("Display with id $displayId doesn't exists")
        data[displayId] = item.copy(_name = name)
    }

    override fun createDisplay(name: String, dashboardId: DashboardId): DisplayId {
        val newId = "id${data.size}"
        data[newId] = DataDisplay(name, newId)
        mapping[newId] = dashboardId
        return newId
    }

    override fun allDisplays(): List<Entities.Display> {
        return data.values.toList()
    }
}