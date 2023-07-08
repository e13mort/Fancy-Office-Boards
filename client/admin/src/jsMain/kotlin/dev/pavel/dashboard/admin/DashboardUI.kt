package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.admin.dashboards.DashboardPM
import dev.petuska.kmdc.button.Icon
import dev.petuska.kmdc.button.Label
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.elevation.MDCElevation
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
        RenderTitle(currentPMState)
        RenderLinks(currentPMState)
        RenderButtons()
    }
}

@Composable
private fun DashboardPM.RenderLinks(currentPMState: DashboardPM.State) {
    val targets = targetsProp.collectAsState().value
    println("Targets recomposition: $targets")
    targets.forEachIndexed { index, link ->
        Div(attrs = {
            style {
                paddingBottom(4.px)
            }
        }) {
            val disabled = when (currentPMState) {
                DashboardPM.State.View -> true
                DashboardPM.State.Edit -> false
            }
            RenderLink(link, index, disabled) {
                if (!disabled) {
                    RenderButton(
                        LinkButton.Up, index, index == 0
                    )
                    RenderButton(
                        LinkButton.Down, index, index == targets.size - 1
                    )
                    RenderButton(
                        LinkButton.Remove, index, false
                    )
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
private fun DashboardPM.RenderLink(
    link: String, index: Int, disabled: Boolean, trailingContent: @Composable () -> Unit
) {
    MDCTextField(link,
        type = MDCTextFieldType.Outlined,
        maxLength = 1024.toUInt(),
        disabled = disabled,
        label = "Link ${index + 1}",
        attrs = {
            onInput { inputEvent ->
                updateLink(index, inputEvent.value)
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
    }
}

@Composable
private fun DashboardPM.RenderButton(button: CardButton) {
    MDCButton(attrs = {
        onClick {
            when (button) {
                CardButton.Cancel -> handleCancelClick()
                CardButton.Edit -> handleActionClick()
                CardButton.Save -> handleActionClick()
                CardButton.Create -> handleActionClick()
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
private fun DashboardPM.RenderButton(
    button: LinkButton, index: Int, disabled: Boolean
) {
    MDCButton(attrs = {
        if (disabled) disabled()
        onClick {
            when (button) {
                LinkButton.Up -> moveLink(index, DashboardPM.Direction.Up)
                LinkButton.Down -> moveLink(index, DashboardPM.Direction.Down)
                LinkButton.Remove -> removeLink(index)
            }
        }
    }) {
        Icon(attrs = {
            mdcIcon()
        }) { Text(button.iconType.type) }
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
}

private enum class LinkButton(
    val iconType: MDCIcon
) {
    Up(MDCIcon.ArrowUpward),
    Down(MDCIcon.ArrowDownward),
    Remove(MDCIcon.Remove),
}
