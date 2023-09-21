package dev.pavel.dashboard.admin.displays

import dev.pavel.dashboard.admin.dashboards.EditableCollectionPM
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.DataItemsInteractor
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams

class DisplaysPM(
    params: PmParams,
    dataItemsInteractor: DataItemsInteractor<Entities.Display>,
    private val dashboardsInteractor: DataItemsInteractor<out Entities.Dashboard>
) : EditableCollectionPM<Entities.Display, DisplayPM>(
    pmParams = params,
    dataItemsInteractor = dataItemsInteractor,
) {
    @Serializable
    object Description : PmDescription

    private var allDataItems: List<Entities.Dashboard> = emptyList()

    override suspend fun onLoad() {
        allDataItems = dashboardsInteractor.allDataItems()
    }

    override fun createChildPm(model: Entities.Display?): PmDescription {
        return if (model == null) {
            DisplayPM.Description(
                availableDashboards = allDataItems
            )
        } else {
            DisplayPM.Description(
                id = model.id(),
                displayName = model.name(),
                description = model.description(),
                dashboardId = model.dashboardId(),
                availableDashboards = allDataItems
            )
        }

    }
}