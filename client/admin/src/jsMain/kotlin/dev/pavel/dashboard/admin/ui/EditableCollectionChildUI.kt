package dev.pavel.dashboard.admin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.admin.EditableCollectionChildPM
import dev.petuska.kmdc.button.Icon
import dev.petuska.kmdc.button.Label
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.elevation.MDCElevation
import dev.petuska.kmdc.linear.progress.MDCLinearProgress
import dev.petuska.kmdcx.icons.MDCIcon
import dev.petuska.kmdcx.icons.mdcIcon
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
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
            when (currentState) {
                is EditableCollectionChildPM.State.Edit -> currentState.item.renderContent(true)
                is EditableCollectionChildPM.State.Saving -> currentState.item.renderContent(false)
                is EditableCollectionChildPM.State.View -> currentState.item.renderContent(false)
            }
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
        }

        is EditableCollectionChildPM.State.Saving -> {
            RenderButton(CardButton.Updating, true) { }
        }
    }
}

@Composable
private fun RenderButton(button: CardButton, disabled: Boolean = false, click: () -> Unit) {
    MDCButton(attrs = {
        if (disabled) {
            disabled()
        }
        onClick {
            click()
        }
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
}

@Composable
private fun RenderUpdatingLoader() {
    MDCLinearProgress {

    }
}