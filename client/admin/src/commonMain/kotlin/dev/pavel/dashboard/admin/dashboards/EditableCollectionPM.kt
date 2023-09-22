package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.admin.EditableCollectionChildPM
import dev.pavel.dashboard.interactors.DataItemsInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel
import me.dmdev.premo.navigation.BackMessage

abstract class EditableCollectionPM<ENTITY_TYPE, CHILD_PM: PresentationModel>(
    pmParams: PmParams,
    private val dataItemsInteractor: DataItemsInteractor<ENTITY_TYPE>,
) : PresentationModel(pmParams) {
    private val _flow = MutableStateFlow<State>(LOADING)
    private var currentChildren: List<CHILD_PM> = emptyList()
    init {
        messageHandler.addHandler { msg ->
            when(msg) {
                is BackMessage -> {
                    removeNewItems()
                    true
                }
                is EditableCollectionChildPM.ChildBecameInvalid -> {
                    reloadItems()
                    true
                }
                else -> false
            }
        }
    }

    abstract fun createChildPm(model: ENTITY_TYPE?): PmDescription

    private fun removeNewItems() {
        when(val state = _flow.value) {
            is EditableCollectionPM<*, *>.ItemsState -> {
                _flow.value = state.copyAllExceptNew {
                    detachChild(it)
                }
            }
            LOADING -> Unit
        }
    }

    suspend fun load() {
        detachItems()
        _flow.value = LOADING
        onLoad()
        val items: List<ENTITY_TYPE> = dataItemsInteractor.allDataItems()
        _flow.value = ItemsState(items = wrapAndAttachChildren(items).also {
            currentChildren = it
        })
    }

    open suspend fun onLoad() = Unit

    private fun reloadItems() {
        scope.launch {
            load()
        }
    }

    private fun detachItems() {
        val value = _flow.value
        if (value is EditableCollectionPM<*, *>.ItemsState) {
            for (dashboard in value.items) {
                detachChild(dashboard)
            }
        }
    }

    private fun wrapAndAttachChildren(items: List<ENTITY_TYPE>): List<CHILD_PM> {
        return items.map {
            Child<CHILD_PM>(createChildPm(it)).also { pm ->
                attachChild(pm)
            }
        }
    }

    fun dashboards(): StateFlow<State> {
        return _flow
    }

    fun addNewDashboard() {
        val items = currentChildren.toMutableList().also {
            val newItem: CHILD_PM = Child(createChildPm(null))
            attachChild(newItem)
            it.add(newItem)
        }.toList()
        _flow.value = ItemsState(items, items.size - 1)
    }

    sealed interface State

    object LOADING : State

    inner class ItemsState(
        val items: List<CHILD_PM>,
        private val newItemIndex: Int = -1
    ) : State {
        fun addButtonEnabled(): Boolean {
            return newItemIndex == -1
        }

        internal fun copyAllExceptNew(cleanUp: (CHILD_PM) -> Unit): ItemsState {
            val newItems = if (newItemIndex != -1) {
                items.toMutableList().apply {
                    cleanUp(removeAt(newItemIndex))
                }.toList()
            } else items
            return ItemsState(newItems)
        }
    }
}