package dev.pavel.dashboard.admin.displays

import dev.pavel.dashboard.admin.dashboards.EditableCollectionPM
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.DataItemsInteractor
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams

class DisplaysPM(
    params: PmParams,
    dataItemsInteractor: DataItemsInteractor<Entities.Display>
) : EditableCollectionPM<Entities.Display, DisplayPM>(
    pmParams = params,
    dataItemsInteractor = dataItemsInteractor,
    childPMFactory = { model ->
        if (model == null) {
            DisplayPM.Description()
        } else {
            DisplayPM.Description(model.id(), model.name())
        }
    }
) {
    @Serializable
    object Description : PmDescription
}