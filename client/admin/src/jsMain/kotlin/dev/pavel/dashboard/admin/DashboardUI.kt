package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.admin.dashboards.DashboardPM
import dev.petuska.kmdc.button.Icon
import dev.petuska.kmdc.button.Label
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.elevation.MDCElevation
import dev.petuska.kmdc.linear.progress.MDCLinearProgress
import dev.petuska.kmdc.textfield.MDCTextField
import dev.petuska.kmdc.textfield.MDCTextFieldType
import dev.petuska.kmdc.typography.MDCBody1
import dev.petuska.kmdcx.icons.MDCIcon
import dev.petuska.kmdcx.icons.mdcIcon
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun DashboardPM.Render() {
    MDCElevation(z = 3, attrs = {
        style {
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
            padding(8.px)
        }
    }) {
        val currentPMState = observeStates().collectAsState().value
        if (currentPMState is DashboardPM.State.Saving) {
            RenderUpdatingLoader()
        }
        currentPMState.RenderTitle()
        currentPMState.RenderLinks()
        currentPMState.RenderButtons()
    }
}

@Composable
private fun DashboardPM.State.RenderLinks() {
    val targetList = targets.states.collectAsState().value
    val state = this
    targetList.forEachIndexed { index, link ->
        Div(attrs = {
            style {
                paddingBottom(4.px)
            }
        }) {
            val disabled = when (state) {
                is DashboardPM.State.View -> true
                is DashboardPM.State.Edit -> false
                is DashboardPM.State.Saving -> true //? should render disabled buttons
            }
            RenderLink(link, index, disabled) {
                if (!disabled) {
                    RenderButton(
                        LinkButton.Up, link.upEnabled
                    ) {
                        targets.handleAction(index, DashboardPM.TargetAction.Up)
                    }
                    RenderButton(
                        LinkButton.Down, link.downEnabled
                    ) {
                        targets.handleAction(index, DashboardPM.TargetAction.Down)
                    }
                    RenderButton(
                        LinkButton.Remove, true
                    ) {
                        targets.handleAction(index, DashboardPM.TargetAction.Remove)
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardPM.State.RenderTitle() {
    val state = this
    Div(
        attrs = {
            style {
                padding(4.px)
            }
        }
    ) {
        when (state) {
            is DashboardPM.State.View -> MDCBody1(name.value)
            is DashboardPM.State.Saving -> MDCBody1(name.value)
            is DashboardPM.State.Edit -> {
                val currentNameValue = name.flow.collectAsState().value
                MDCTextField(
                    value = currentNameValue,
                    label = "Title",
                    attrs = {
                        onInput { syntheticInputEvent ->
                            name.value = syntheticInputEvent.value
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun RenderLink(
    link: DashboardPM.TargetState,
    index: Int,
    disabled: Boolean,
    trailingContent: @Composable () -> Unit
) {
    val linkContent = link.target.flow.collectAsState().value
    MDCTextField(linkContent,
        type = MDCTextFieldType.Outlined,
        maxLength = 1024.toUInt(),
        disabled = disabled,
        label = "Link ${index + 1}",
        attrs = {
            onInput { inputEvent ->
                link.target.value = inputEvent.value
            }
        },
        trailingIcon = {
            trailingContent()
        })
}

@Composable
private fun DashboardPM.State.RenderButtons() {
    when (val value = this) {
        is DashboardPM.State.View -> {
            RenderButton(CardButton.Edit) { value.edit() }
        }
        is DashboardPM.State.Edit -> {
            if (value.isNew)
                RenderButton(CardButton.Create) { value.save() }
            else
                RenderButton(CardButton.Save) { value.save() }
            RenderButton(CardButton.Cancel) { value.cancel() }
        }

        is DashboardPM.State.Saving -> {
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

@Composable
private fun RenderButton(
    button: LinkButton, enabled: Boolean, callBack: () -> Unit
) {
    MDCButton(attrs = {
        if (!enabled) disabled()
        onClick {
            callBack()
        }
    }) {
        Icon(attrs = {
            mdcIcon()
        }) { Text(button.iconType.type) }
    }

}

@Composable
private fun RenderUpdatingLoader() {
    MDCLinearProgress {

    }
}

private enum class CardButton(
    val title: String,
    val iconType: MDCIcon
) {
    Cancel("Cancel", MDCIcon.Cancel),
    Edit("Edit", MDCIcon.Edit),
    Save("Save", MDCIcon.Save),
    Create("Create", MDCIcon.Create),
    Updating("Saving...", MDCIcon.Create),
}

private enum class LinkButton(
    val iconType: MDCIcon
) {
    Up(MDCIcon.ArrowUpward),
    Down(MDCIcon.ArrowDownward),
    Remove(MDCIcon.Remove),
}
