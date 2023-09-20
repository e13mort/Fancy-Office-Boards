package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.DataItemsInteractor
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams

class DashboardsPM(
    params: PmParams,
    dataItemsInteractor: DataItemsInteractor<Entities.WebPagesDashboard>
) : EditableCollectionPM<Entities.WebPagesDashboard, DashboardPM>(
    pmParams = params,
    dataItemsInteractor = dataItemsInteractor
) {
    @Serializable
    object Description : PmDescription

    override fun createChildPm(model: Entities.WebPagesDashboard?): PmDescription {
        return if (model == null) {
            DashboardPM.Description()
        } else {
            DashboardPM.Description(model.id(), model.name(), model.targets())
        }
    }
}