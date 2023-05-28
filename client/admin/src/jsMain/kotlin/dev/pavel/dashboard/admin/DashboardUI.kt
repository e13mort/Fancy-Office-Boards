package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
            RenderLink(link, index, disabled)
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
private fun RenderLink(link: String, index: Int, disabled: Boolean) {
    var text by remember { mutableStateOf(link) }
    MDCTextField(
        text,
        type = MDCTextFieldType.Outlined,
        maxLength = 1024.toUInt(),
        disabled = disabled,
        label = "Link ${index + 1}",
        attrs = {
            onInput { inputEvent ->
                text = inputEvent.value
            }
        })
}

@Composable
private fun DashboardPM.RenderButtons() {
    when(states.value) {
        DashboardPM.State.View -> {
            RenderButton(Button.Edit)
        }
        DashboardPM.State.Edit -> {
            if (isNew())
                RenderButton(Button.Create)
            else
                RenderButton(Button.Save)
            RenderButton(Button.Cancel)
        }
    }
}

@Composable
private fun DashboardPM.RenderButton(button: Button) {
    MDCButton(attrs = {
        onClick {
            when(button) {
                Button.Cancel -> handleCancelClick()
                Button.Edit -> handleActionClick()
                Button.Save -> handleActionClick()
                Button.Create -> handleActionClick()
            }
        }
    }) {
        Icon(attrs = {
            mdcIcon()
        }) { Text(button.iconType.type) }
        Label(button.title)
    }
}

private enum class Button(
    val title: String,
    val iconType: MDCIcon
) {
    Cancel("Cancel", MDCIcon.Cancel),
    Edit("Edit", MDCIcon.Edit),
    Save("Save", MDCIcon.Save),
    Create("Create", MDCIcon.Create)
}
