package dev.pavel.dashboard.admin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.admin.EditableCollectionChildPM
import dev.petuska.kmdc.button.Icon
import dev.petuska.kmdc.button.Label
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.dialog.Action
import dev.petuska.kmdc.dialog.Actions
import dev.petuska.kmdc.dialog.Content
import dev.petuska.kmdc.dialog.MDCDialog
import dev.petuska.kmdc.dialog.onClosed
import dev.petuska.kmdc.elevation.MDCElevation
import dev.petuska.kmdc.linear.progress.MDCLinearProgress
import dev.petuska.kmdc.snackbar.Label
import dev.petuska.kmdc.snackbar.MDCSnackbar
import dev.petuska.kmdcx.icons.MDCIcon
import dev.petuska.kmdcx.icons.mdcIcon
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun <T> EditableCollectionChildPM<T>.RenderCollection(
    renderContent: @Composable T.(Boolean) -> Unit
) {
    MDCElevation(z = 3, attrs = {
        style {
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
            padding(8.px)
        }
    }) {
        val currentState = observeStates().collectAsState().value
        if (currentState is EditableCollectionChildPM.State.Saving) {
            RenderUpdatingLoader()
        }
        Div {
            val isActive = when (currentState) {
                is EditableCollectionChildPM.State.Edit -> true
                is EditableCollectionChildPM.State.Saving -> false
                is EditableCollectionChildPM.State.View -> false
            }
            currentState.item.renderContent(isActive)
        }
        currentState.RenderButtons()
    }

}

@Composable
private fun EditableCollectionChildPM.State<*>.RenderButtons() {
    when (val value = this) {
        is EditableCollectionChildPM.State.View -> {
            RenderButton(CardButton.Edit) { value.edit() }
        }
        is EditableCollectionChildPM.State.Edit -> {
            if (value.isNew)
                RenderButton(CardButton.Create) { value.save() }
            else
                RenderButton(CardButton.Save) { value.save() }
            RenderButton(CardButton.Cancel) { value.cancel() }
            if (!value.isNew) {
                RenderButton(CardButton.Delete, styleBlock = {
                    property("float", "right")
                }) { value.delete(EditableCollectionChildPM.DeleteAction.Request) }
                RenderDeleteDialog(value)
            }
            RenderError(value)
        }
        is EditableCollectionChildPM.State.Saving -> {
            RenderButton(CardButton.Updating, true) { }
        }
    }
}

@Composable
private fun RenderError(value: EditableCollectionChildPM.State.Edit<*>) {
    MDCSnackbar(open = value.activeError, timeoutMs = null, content = {
        Label("An error occurred")
    })
}

@Composable
private fun RenderDeleteDialog(value: EditableCollectionChildPM.State.Edit<*>) {
    MDCDialog(
        open = value.showDeleteDialog,
        stacked = false,
        attrs = {
            onClosed { eventDetailMDCEvent ->
                val action = when (eventDetailMDCEvent.detail.action) {
                    "ok" -> EditableCollectionChildPM.DeleteAction.Confirm
                    else -> EditableCollectionChildPM.DeleteAction.Cancel
                }
                value.delete(action)
            }
        }
    ) {
        Content {
            Text("Delete item?")
        }
        Actions {
            Action("ok", "Ok")
            Action("close", "Cancel")
        }
    }
}

@Composable
private fun RenderButton(
    button: CardButton,
    disabled: Boolean = false,
    styleBlock: StyleScope.() -> Unit = {},
    click: () -> Unit
) {
    MDCButton(attrs = {
        if (disabled) {
            disabled()
        }
        onClick {
            click()
        }
        style(styleBlock)
    }) {
        Icon(attrs = {
            mdcIcon()
        }) { Text(button.iconType.type) }
        Label(button.title)
    }
}

enum class CardButton(
    val title: String,
    val iconType: MDCIcon
) {
    Cancel("Cancel", MDCIcon.Cancel),
    Edit("Edit", MDCIcon.Edit),
    Save("Save", MDCIcon.Save),
    Create("Create", MDCIcon.Create),
    Updating("Saving...", MDCIcon.Create),
    Add("Add", MDCIcon.Add),
    Delete("Delete", MDCIcon.Delete),
}

@Composable
private fun RenderUpdatingLoader() {
    MDCLinearProgress {

    }
}