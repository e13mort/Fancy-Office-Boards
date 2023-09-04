@file:Suppress("unused")

package dev.pavel.dashboard.admin.displays

import dev.pavel.dashboard.entity.DisplayId
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel

class DisplayPM(pmParams: PmParams) : PresentationModel(params = pmParams) {
    class Description(
        val id: DisplayId? = null,
        val name: String = ""
    ) :
        PmDescription
}