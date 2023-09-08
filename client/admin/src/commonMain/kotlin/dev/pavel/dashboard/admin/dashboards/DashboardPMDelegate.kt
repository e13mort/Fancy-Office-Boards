package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.admin.EditableCollectionChildPM
import dev.pavel.dashboard.admin.properties.StringProperty
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

    override fun copy(item: DashboardPM.Description): DashboardPM.Description {
        return item.copy(
            id = item.id.toString(),
            name = StringProperty(item.name.value),
            targets = DashboardPM.TargetsImpl(item.targets.states.value.toTargets())
        )

    }

    override suspend fun save(item: DashboardPM.Description): DashboardPM.Description {
        val id = item.id
        val value: List<DashboardPM.TargetState> = item.targets.states.value
        val dashboard = if (id != null) {
            updateDashboardInteractor.updateDashboard(
                id,
                value.toTargets(),
                item.name.value
            )
        } else {
            createDashboardInteractor.createDashboard(
                value.toTargets(),
                item.name.value
            )
        }
        return DashboardPM.Description(dashboard.id(), dashboard.name(), dashboard.targets())
    }

    override fun isNew(item: DashboardPM.Description): Boolean {
        return item.id == null
    }

}