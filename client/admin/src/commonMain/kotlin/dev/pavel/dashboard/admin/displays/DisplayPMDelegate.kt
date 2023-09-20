package dev.pavel.dashboard.admin.displays

import dev.pavel.dashboard.admin.EditableCollectionChildPM
import dev.pavel.dashboard.admin.properties.ListProperty
import dev.pavel.dashboard.admin.properties.StringProperty
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.DataItemsInteractor
import dev.pavel.dashboard.interactors.UpdateDisplayInteractor
import me.dmdev.premo.PmDescription

class DisplayPMDelegate(
    private val updateDisplayInteractor: UpdateDisplayInteractor,
    private val dashboardDataItemsInteractor: DataItemsInteractor<out Entities.Dashboard>
) : EditableCollectionChildPM.ChildDelegate<DisplayPM.Description> {
    override fun create(description: PmDescription): DisplayPM.Description {
        return description as DisplayPM.Description
    }

    override fun copy(item: DisplayPM.Description): DisplayPM.Description {
        return item.copy(
            id = item.id.toString(),
            name = StringProperty(item.name.value),
            description = StringProperty(item.description.value),
            dashboard = ListProperty(item.dashboard.value?.data, item.dashboard.listItems)
        )
    }

    override suspend fun save(item: DisplayPM.Description): DisplayPM.Description {
        val updatedDisplay = updateDisplayInteractor.createOrUpdateDisplay(
            item.id,
            item.name.value,
            item.description.value,
            item.dashboard.value?.data
        )
        updatedDisplay.first.id() to updatedDisplay.second?.id()
        return DisplayPM.Description(
            id = updatedDisplay.first.id(),
            displayName = updatedDisplay.first.name(),
            description = updatedDisplay.first.description(),
            dashboardId = updatedDisplay.second?.id(),
            dashboardDataItemsInteractor.allDataItems()
        )
    }

    override fun isNew(item: DisplayPM.Description): Boolean {
        return item.id == null
    }

}