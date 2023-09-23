package dev.pavel.dashboard.admin

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmMessage
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
        suspend fun delete(item: T)
    }

    private var activeData = delegate.create(pmParams.description)
    private var originalData = delegate.copy(activeData)
    private var editTransitionJob: Job? = null

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
            val view = trySaveData(value)
            states.value = view
        }
    }

    private suspend fun trySaveData(value: T): State<T> {
        return try {
            val updateValue = delegate.save(value)
            activeData = updateValue
            originalData = delegate.copy(updateValue)
            State.View(updateValue, this)
        } catch (e: Exception) {
            scheduleEditTransition()
            State.Edit(states.value.item, this, activeError = true)
        }
    }

    private fun scheduleEditTransition() {
        editTransitionJob?.cancel()
        editTransitionJob = scope.launch {
            delay(2000)
            states.value = State.Edit(states.value.item, this@EditableCollectionChildPM)
        }
    }

    internal fun cancel() {
        editTransitionJob?.cancel()
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

    private fun delete(type: DeleteAction) {
        when (type) {
            DeleteAction.Request -> states.value = State.Edit(
                item = states.value.item,
                host = this,
                showDeleteDialog = true
            )
            DeleteAction.Confirm -> deleteCurrentItem()
            DeleteAction.Cancel -> states.value = State.Edit(states.value.item, this)
        }
    }

    private fun deleteCurrentItem() {
        scope.launch {
            val currentItem = states.value.item
            states.value = State.Saving(currentItem)
            delegate.delete(currentItem)
            messageHandler.send(ChildBecameInvalid)
        }
    }

    enum class DeleteAction {
        Request, Confirm, Cancel
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
            val showDeleteDialog: Boolean = false,
            val activeError: Boolean = false
        ) : State<T>(item) {
            val isNew: Boolean = host.delegate.isNew(host.activeData)
            fun cancel() {
                host.cancel()
            }

            fun save() {
                host.save()
            }

            fun delete(type: DeleteAction) {
                host.delete(type)
            }
        }

        class Saving<T>(item: T) : State<T>(item)
    }

    object ChildBecameInvalid : PmMessage
}