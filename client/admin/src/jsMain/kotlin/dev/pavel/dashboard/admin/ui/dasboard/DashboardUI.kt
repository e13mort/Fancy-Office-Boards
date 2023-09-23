package dev.pavel.dashboard.admin.ui.dasboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.admin.dashboards.DashboardPM
import dev.pavel.dashboard.admin.ui.CardButton
import dev.pavel.dashboard.admin.ui.RenderCollection
import dev.pavel.dashboard.admin.ui.RenderAsTitle
import dev.pavel.dashboard.admin.ui.RenderFormfield
import dev.petuska.kmdc.button.Icon
import dev.petuska.kmdc.button.Label
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.textfield.MDCTextField
import dev.petuska.kmdc.textfield.MDCTextFieldType
import dev.petuska.kmdcx.icons.MDCIcon
import dev.petuska.kmdcx.icons.mdcIcon
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun DashboardPM.Render() {
    RenderCollection { isActive ->
        name.RenderAsTitle(isActive)
        targets.RenderLinks(isActive)
        switchDelaySeconds.RenderFormfield(isActive, "Timeout (s)")
    }
}

@Composable
private fun DashboardPM.Targets.RenderLinks(
    isActive: Boolean = false
) {
    val targetList = states.collectAsState().value
    targetList.forEachIndexed { index, link ->
        Div(attrs = {
            style {
                paddingBottom(4.px)
            }
        }) {
            RenderLink(link, index, !isActive) {
                if (isActive) {
                    RenderButton(
                        DashboardContentButton.Up, link.upEnabled
                    ) {
                        handleAction(index, DashboardPM.TargetAction.Up)
                    }
                    RenderButton(
                        DashboardContentButton.Down, link.downEnabled
                    ) {
                        handleAction(index, DashboardPM.TargetAction.Down)
                    }
                    RenderButton(
                        DashboardContentButton.Remove, true
                    ) {
                        handleAction(index, DashboardPM.TargetAction.Remove)
                    }
                }
            }
        }
    }
    if (isActive) {
        Div {
            RenderButton(CardButton.Add, !isActive) {
                add()
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
    button: DashboardContentButton, enabled: Boolean, callBack: () -> Unit
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
private enum class DashboardContentButton(
    val iconType: MDCIcon
) {
    Up(MDCIcon.ArrowUpward),
    Down(MDCIcon.ArrowDownward),
    Remove(MDCIcon.Remove),
}
