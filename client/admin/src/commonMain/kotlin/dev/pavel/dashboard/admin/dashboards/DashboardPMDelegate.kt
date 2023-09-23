package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.admin.EditableCollectionChildPM
import dev.pavel.dashboard.admin.properties.copy
import dev.pavel.dashboard.interactors.CreateDashboardInteractor
import dev.pavel.dashboard.interactors.UpdateDashboardInteractor
import me.dmdev.premo.PmDescription

class DashboardPMDelegate(
    private val updateDashboardInteractor: UpdateDashboardInteractor,
    private val createDashboardInteractor: CreateDashboardInteractor,
) : EditableCollectionChildPM.ChildDelegate<DashboardPM.Description> {
    override fun create(description: PmDescription): DashboardPM.Description {
        return description as DashboardPM.Description
    }

    override suspend fun delete(item: DashboardPM.Description) {
        if (item.id == null) return
        updateDashboardInteractor.delete(item.id)
    }

    override fun copy(item: DashboardPM.Description): DashboardPM.Description {
        return item.copy(
            id = item.id.toString(),
            name = item.name.copy(),
            targets = DashboardPM.TargetsImpl(item.targets.states.value.toTargets()),
            switchDelaySeconds = item.switchDelaySeconds.copy()
        )

    }

    override suspend fun save(item: DashboardPM.Description): DashboardPM.Description {
        val id = item.id
        val value: List<DashboardPM.TargetState> = item.targets.states.value
        val dashboard = if (id != null) {
            updateDashboardInteractor.updateDashboard(
                id,
                value.toTargets(),
                item.name.value,
                item.switchDelaySeconds.value.toInt()
            )
        } else {
            createDashboardInteractor.createDashboard(
                value.toTargets(),
                item.name.value,
                item.switchDelaySeconds.value.toInt()
            )
        }
        return DashboardPM.Description(dashboard.id(), dashboard.name(), dashboard.switchTimeoutSeconds(), dashboard.targets())
    }

    override fun isNew(item: DashboardPM.Description): Boolean {
        return item.id == null
    }

}