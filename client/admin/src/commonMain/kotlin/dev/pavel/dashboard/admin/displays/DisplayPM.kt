@file:Suppress("unused")

package dev.pavel.dashboard.admin.displays

import dev.pavel.dashboard.admin.EditableCollectionChildPM
import dev.pavel.dashboard.admin.properties.StringProperty
import dev.pavel.dashboard.entity.DisplayId
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams

class DisplayPM(
    pmParams: PmParams,
    delegate: DisplayPMDelegate
) : EditableCollectionChildPM<DisplayPM.Description>(
    pmParams = pmParams,
    delegate = delegate
) {
    data class Description(
        val id: DisplayId?,
        val name: StringProperty,
        val description: StringProperty
    ) : PmDescription {
        constructor(
            id: DisplayId? = null,
            name: String = "",
            description: String = ""
        ) : this(id, StringProperty(name), StringProperty(description))

    }
}