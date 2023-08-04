package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.CreateDashboardInteractor
import dev.pavel.dashboard.interactors.UpdateDashboardInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmMessage
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel

class DashboardPM(
    params: PmParams,
    private val updateDashboardInteractor: UpdateDashboardInteractor,
    private val createDashboardInteractor: CreateDashboardInteractor
) : PresentationModel(params) {
    private var _description = params.description as Description
    private var _sourceDescription = _description.copy()

    private val states: MutableStateFlow<State> = MutableStateFlow(
        when {
            isNew() -> createEditPMState(true)
            else -> BaseViewStateImpl.ViewImpl(this)
        }
    )

    fun observeStates(): StateFlow<State> = states

    fun isNew() = _description.id == null

    private fun edit() {
        states.value = createEditPMState(isNew())
    }

    private fun handleCancelClick() {
        if (isNew()) {
            cancel()
        } else {
            restoreData()
            states.value = BaseViewStateImpl.ViewImpl(this)
        }
    }

    private fun createEditPMState(isNew: Boolean): State.Edit {
        return BaseViewStateImpl.EditImpl(this, isNew)
    }

    private fun updateDashboard(name: String, targets: Targets) {
        scope.launch {
            states.value = BaseViewStateImpl.Saving(states.value)
            try {
                val id = _description.id
                val dashboard = if (id != null) {
                    updateDashboardInteractor.updateDashboard(
                        id, targets.states.value.toTargets(), name
                    )
                } else {
                    createDashboardInteractor.createDashboard(
                        targets.states.value.toTargets(), name
                    )
                }
                updateData(dashboard)
            } catch (e: Exception) {
                //todo: show error
            }
            states.value = BaseViewStateImpl.ViewImpl(this@DashboardPM)
        }
    }

    private fun updateData(dashboard: Entities.WebPagesDashboard) {
        _description = Description(
            id = dashboard.id(), name = dashboard.name(), targets = dashboard.targets()
        )
        _sourceDescription = _description.copy()
    }

    private fun restoreData() {
        _description = _sourceDescription.copy()
    }


    private fun cancel() {
        messageHandler.send(CancelMessage)
    }

    private fun List<TargetState>.toTargets(): List<String> {
        return map { targetState ->
            targetState.target.value
        }
    }

    object CancelMessage : PmMessage

    sealed interface State {
        val name: ViewProperty<String>

        val targets: Targets
        interface View : State {
            fun edit()
        }

        interface Edit : State {
            fun save()
            fun cancel()

            val isNew: Boolean
        }

        interface Saving : State
    }

    open class BaseViewStateImpl(description: Description) {
        val name by lazy {
            StringProperty(description.name)
        }
        val targets: Targets by lazy {
            TargetsImpl(description.targets)
        }
        data class ViewImpl(
            private val pm: DashboardPM
        ) : BaseViewStateImpl(pm._description), State.View {
            override fun edit() {
                pm.edit()
            }
        }

        class EditImpl(
            private val pm: DashboardPM,
            override val isNew: Boolean = false,
            val addLinkEnabled: Boolean = false
        ) : BaseViewStateImpl(pm._description), State.Edit {

            override fun save() {
                pm.updateDashboard(
                    name.value,
                    targets
                )
            }

            override fun cancel() {
                pm.handleCancelClick()
            }
        }

        class Saving(private val dashboardViewState: State) : State.Saving {
            override val name: ViewProperty<String>
                get() = dashboardViewState.name
            override val targets: Targets
                get() = dashboardViewState.targets

        }
    }

    enum class TargetAction {
        Up, Down, Remove
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

    data class Description(
        val id: DashboardId? = null,
        val name: String = "",
        val targets: List<String> = emptyList()
    ) : PmDescription

}

private fun List<String>.toStates(): List<DashboardPM.TargetStateImpl> {
    val result = mutableListOf<DashboardPM.TargetStateImpl>()
    forEachIndexed { _, rawTarget ->
        result += DashboardPM.TargetStateImpl(
            StringProperty(rawTarget)
        )
    }
    return result
}