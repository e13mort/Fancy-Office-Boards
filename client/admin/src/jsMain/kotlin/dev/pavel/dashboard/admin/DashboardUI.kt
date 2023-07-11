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
        val currentPMState = states.collectAsState().value
        if (currentPMState == DashboardPM.State.Saving) {
            RenderUpdatingLoader()
        }
        RenderTitle(currentPMState)
        RenderLinks(currentPMState)
        RenderButtons()
    }
}

@Composable
private fun DashboardPM.RenderLinks(currentPMState: DashboardPM.State) {
    val targets = targetsProp.collectAsState().value
    targets.forEachIndexed { index, link ->
        Div(attrs = {
            style {
                paddingBottom(4.px)
            }
        }) {
            val disabled = when (currentPMState) {
                DashboardPM.State.View -> true
                DashboardPM.State.Edit -> false
                DashboardPM.State.Saving -> true //? should render disabled buttons
            }
            RenderLink(link, index, disabled) {
                if (!disabled) {
                    RenderButton(
                        LinkButton.Up, link.upEnabled
                    ) {
                        link.actionCallback(DashboardPM.TargetAction.Up)
                    }
                    RenderButton(
                        LinkButton.Down, link.downEnabled
                    ) {
                        link.actionCallback(DashboardPM.TargetAction.Down)
                    }
                    RenderButton(
                        LinkButton.Remove, false
                    ) {
                        link.actionCallback(DashboardPM.TargetAction.Remove)
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardPM.RenderTitle(currentPMState: DashboardPM.State) {
    Div(
        attrs = {
            style {
                padding(4.px)
            }
        }
    ) {
        when (currentPMState) {
            DashboardPM.State.View -> MDCBody1(name)
            DashboardPM.State.Saving -> MDCBody1(name)
            DashboardPM.State.Edit -> {
                MDCTextField(
                    value = name,
                    label = "Title"
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
    MDCTextField(link.target,
        type = MDCTextFieldType.Outlined,
        maxLength = 1024.toUInt(),
        disabled = disabled,
        label = "Link ${index + 1}",
        attrs = {
            onInput { inputEvent ->
                link.updateContentCallback(inputEvent.value)
            }
        },
        trailingIcon = {
            trailingContent()
        })
}

@Composable
private fun DashboardPM.RenderButtons() {
    when (states.value) {
        DashboardPM.State.View -> {
            RenderButton(CardButton.Edit)
        }
        DashboardPM.State.Edit -> {
            if (isNew())
                RenderButton(CardButton.Create)
            else
                RenderButton(CardButton.Save)
            RenderButton(CardButton.Cancel)
        }

        DashboardPM.State.Saving -> {
            RenderButton(CardButton.Updating, true)
        }
    }
}

@Composable
private fun DashboardPM.RenderButton(button: CardButton, disabled: Boolean = false) {
    MDCButton(attrs = {
        if (disabled) {
            disabled()
        }
        onClick {
            when (button) {
                CardButton.Cancel -> handleCancelClick()
                CardButton.Edit -> handleActionClick()
                CardButton.Save -> handleActionClick()
                CardButton.Create -> handleActionClick()
                CardButton.Updating -> {  }
            }
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
    button: LinkButton, disabled: Boolean, callBack: () -> Unit
) {
    MDCButton(attrs = {
        if (disabled) disabled()
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
