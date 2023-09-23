/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.admin.EditableCollectionChildPM
import dev.pavel.dashboard.admin.properties.validation.PositiveIntValidator
import dev.pavel.dashboard.admin.properties.StringProperty
import dev.pavel.dashboard.entity.DashboardId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams


class DashboardPM(
    params: PmParams,
    delegate: DashboardPMDelegate
) : EditableCollectionChildPM<DashboardPM.Description>(
    pmParams = params,
    delegate = delegate
) {


    data class Description(
        val id: DashboardId?,
        val name: StringProperty,
        val switchDelaySeconds: StringProperty,
        val targets: Targets = Targets
    ) : PmDescription {
        constructor(
            id: DashboardId? = null,
            name: String = "",
            delaySeconds: Int = 60,
            targets: List<String> = emptyList()
        ) :
                this(id, StringProperty(name), StringProperty(delaySeconds.toString(), listOf(
                    PositiveIntValidator
                )), TargetsImpl(targets))
    }

    enum class TargetAction {
        Up, Down, Remove
    }

    interface Targets {
        val states: StateFlow<List<TargetState>>
        fun handleAction(index: Int, action: TargetAction)

        fun add()

        companion object : Targets {
            override val states: StateFlow<List<TargetState>>
                get() = MutableStateFlow(emptyList())

            override fun handleAction(index: Int, action: TargetAction) = Unit
            override fun add() = Unit

        }
    }

    class TargetsImpl(
        sourceList: List<String>
    ) : Targets {

        override val states: MutableStateFlow<List<TargetStateImpl>> by lazy {
            MutableStateFlow(sourceList.toStates().apply {
                updateButtons()
            }.toList())
        }
        override fun handleAction(index: Int, action: TargetAction) {
            when (action) {
                TargetAction.Up -> moveLink(index, true)
                TargetAction.Down -> moveLink(index, false)
                TargetAction.Remove -> removeLinkAt(index)
            }
        }

        override fun add() {
            states.value = states.value.toMutableList().apply {
                this += TargetStateImpl(StringProperty(""))
                updateButtons()
            }.toList()
        }

        private fun moveLink(index: Int, up: Boolean) {
            val states = states.value.toMutableList()
            val item = states.removeAt(index)
            val newIndex = if (up) {
                index - 1
            } else {
                index + 1
            }.coerceAtLeast(0)
            states.add(newIndex, item)
            states.updateButtons()
            this.states.value = states.toList()
        }

        private fun List<TargetStateImpl>.updateButtons() {
            forEachIndexed { index, targetStateImpl ->
                when(index) {
                    0 -> {
                        targetStateImpl.upEnabled = false
                        targetStateImpl.downEnabled = size > 1
                    }
                    size - 1 -> {
                        targetStateImpl.upEnabled = size > 1
                        targetStateImpl.downEnabled = false
                    }
                    else -> {
                        targetStateImpl.upEnabled = true
                        targetStateImpl.downEnabled = true
                    }
                }
            }
        }

        private fun removeLinkAt(index: Int) {
            val result: MutableList<TargetStateImpl> = states.value.toMutableList()
            result.removeAt(index)
            states.value = result.toList()
        }

    }

    interface TargetState {
        val target: StringProperty
        val upEnabled: Boolean
        val downEnabled: Boolean
    }

    data class TargetStateImpl(
        override val target: StringProperty,
        override var upEnabled: Boolean = false,
        override var downEnabled: Boolean = false,
    ) : TargetState
}

internal fun List<String>.toStates(): List<DashboardPM.TargetStateImpl> {
    val result = mutableListOf<DashboardPM.TargetStateImpl>()
    forEachIndexed { _, rawTarget ->
        result += DashboardPM.TargetStateImpl(
            StringProperty(rawTarget)
        )
    }
    return result
}

internal fun List<DashboardPM.TargetState>.toTargets(): List<String> {
    return map { targetState ->
        targetState.target.value
    }
}