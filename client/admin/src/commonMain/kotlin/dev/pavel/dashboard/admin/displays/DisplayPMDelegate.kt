package dev.pavel.dashboard.admin.displays

import dev.pavel.dashboard.admin.EditableCollectionChildPM
import dev.pavel.dashboard.admin.properties.StringProperty
import kotlinx.coroutines.delay
import me.dmdev.premo.PmDescription

class DisplayPMDelegate : EditableCollectionChildPM.ChildDelegate<DisplayPM.Description> {
    override fun create(description: PmDescription): DisplayPM.Description {
        return description as DisplayPM.Description
    }

    override fun copy(item: DisplayPM.Description): DisplayPM.Description {
        return item.copy(
            id = item.id.toString(),
            name = StringProperty(item.name.value)
        )
    }

    override suspend fun save(item: DisplayPM.Description): DisplayPM.Description {
        delay(500)
        return item.copy()
    }

    override fun isNew(item: DisplayPM.Description): Boolean {
        return item.id == null
    }

}