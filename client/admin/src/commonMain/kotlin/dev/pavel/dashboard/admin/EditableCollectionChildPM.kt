package dev.pavel.dashboard.admin

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel
import me.dmdev.premo.navigation.BackMessage

abstract class EditableCollectionChildPM<T>(
    pmParams: PmParams,
    private val delegate: ChildDelegate<T>
) : PresentationModel(params = pmParams) {
    interface ChildDelegate<T> {
        fun create(description: PmDescription): T
        fun isNew(item: T): Boolean
        suspend fun save(item: T): T
        fun copy(item: T): T
    }

    private var activeData = delegate.create(pmParams.description)
    private var originalData = delegate.copy(activeData)

    private val states: MutableStateFlow<State<T>> = MutableStateFlow(createFirstState())

    private fun createFirstState() = when {
        delegate.isNew(activeData) -> State.Edit(activeData, this)
        else -> State.View(activeData, this)
    }

    fun observeStates(): StateFlow<State<T>> = states

    internal fun save() {
        scope.launch {
            val value = states.value.item
            states.value = State.Saving(value)
            val updateValue = delegate.save(value)
            activeData = updateValue
            originalData = delegate.copy(updateValue)
            states.value = State.View(updateValue, this@EditableCollectionChildPM)
        }
    }

    internal fun cancel() {
        if (delegate.isNew(activeData)) {
            messageHandler.send(BackMessage)
        } else {
            activeData = delegate.copy(originalData)
            states.value = State.View(activeData, this)
        }
    }

    internal fun edit() {
        states.value = State.Edit(states.value.item, this)
    }

    sealed class State<T>(
        val item: T
    ) {
        class View<T>(item: T, private val host: EditableCollectionChildPM<T>) : State<T>(item) {
            fun edit() {
                host.edit()
            }
        }

        class Edit<T>(
            item: T,
            private val host: EditableCollectionChildPM<T>,
        ) : State<T>(item) {
            val isNew: Boolean = host.delegate.isNew(host.activeData)
            fun cancel() {
                host.cancel()
            }

            fun save() {
                host.save()
            }
        }

        class Saving<T>(item: T) : State<T>(item)
    }
}