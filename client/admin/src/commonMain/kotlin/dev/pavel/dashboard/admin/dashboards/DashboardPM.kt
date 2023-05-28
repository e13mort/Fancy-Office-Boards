package dev.pavel.dashboard.admin.dashboards

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmMessage
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel

class DashboardPM(private val params: PmParams) : PresentationModel(params) {
    val name = _description.name
    val targets = _description.targets.toMutableList()
    val states = MutableStateFlow(
        when {
            isNew() -> State.Edit
            else -> State.View
        }
    )

    private val _description
        get() = params.description as Description

    fun isNew() = _description.type == Description.Type.NEW

    fun handleCancelClick() {
        if (isNew()) {
            cancel()
        } else {
            states.value = State.View
        }
    }

    fun handleActionClick() {
        when (states.value) {
            State.View -> {
                states.value = State.Edit
            }
            State.Edit -> {
                //launch updating
                states.value = State.View
            }
        }
    }

    private fun cancel() {
        messageHandler.send(CancelMessage)
    }

    object CancelMessage : PmMessage

    enum class State {
        View, Edit
    }

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