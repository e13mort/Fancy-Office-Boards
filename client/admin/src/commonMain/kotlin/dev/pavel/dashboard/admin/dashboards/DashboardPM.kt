package dev.pavel.dashboard.admin.dashboards

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmMessage
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel

class DashboardPM(private val params: PmParams) : PresentationModel(params) {
    private val targets = _description.targets.toMutableList()
    val name = _description.name
    val targetsProp = MutableStateFlow(targets.toStates())
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
            targetsProp.value = targets.toStates()
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

    private fun moveLink(index: Int, up: Boolean): List<TargetState> {
        val activeTargets = targetsProp.value.toMutableList()
        val item = activeTargets.removeAt(index)
        val newIndex = if (up) {
            index - 1
        } else {
            index + 1
        }.coerceAtLeast(0)
        activeTargets.add(newIndex, item)
        return activeTargets
    }

    private fun cancel() {
        messageHandler.send(CancelMessage)
    }

    private fun List<String>.toStates(): List<TargetState> {
        return this.mapIndexed { index: Int, s: String ->
            TargetState(s, index == 0, index == size - 1, { action ->
                val updatedTargets = when (action) {
                    TargetAction.Up -> moveLink(index, true)
                    TargetAction.Down -> moveLink(index, false)
                    TargetAction.Remove -> {
                        targetsProp.value.toMutableList().apply {
                            removeAt(index)
                        }
                    }
                }
                targetsProp.value = updatedTargets
            }) { updatedTarget ->
                val updatedTargets = targetsProp.value.toMutableList()
                updatedTargets[index] = updatedTargets[index].copy(target = updatedTarget)
                targetsProp.value = updatedTargets
                updatedTarget
            }
        }
    }

    object CancelMessage : PmMessage

    enum class State {
        View, Edit
    }

    enum class TargetAction {
        Up, Down, Remove
    }

    data class TargetState(
        val target: String,
        val upEnabled: Boolean,
        val downEnabled: Boolean,
        val actionCallback: (TargetAction) -> Unit,
        val updateContentCallback: (String) -> String
    )

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