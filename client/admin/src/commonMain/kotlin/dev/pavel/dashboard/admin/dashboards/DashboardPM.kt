package dev.pavel.dashboard.admin.dashboards

import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmMessage
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel

class DashboardPM(private val params: PmParams) : PresentationModel(params) {
    val name = _description.name
    val targets = _description.targets

    private val _description
        get() = params.description as Description

    fun isNew() = (params.description as Description).type == Description.Type.NEW

    fun handleClick() {
        if (isNew()) {
            cancel()
        }
    }

    private fun cancel() {
        messageHandler.send(CancelMessage)
    }

    object CancelMessage : PmMessage

    @Serializable
    data class Description(
        val name: String = "",
        val targets: List<String> = emptyList(),
        val type: Type = Type.NEW
    ) : PmDescription {
        enum class Type {
            EXISTING, NEW
        }
    }
}