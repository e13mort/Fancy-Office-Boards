package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dev.pavel.dashboard.admin.dashboards.DashboardPM
import dev.petuska.kmdc.button.Icon
import dev.petuska.kmdc.button.Label
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.button.MDCButtonScope
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
        MDCBody1(name)
        targets.forEachIndexed { index, link ->
            Div(attrs = {
                style {
                    paddingBottom(4.px)
                }
            }) {
                var text by remember { mutableStateOf(link) }
                MDCTextField(
                    text,
                    type = MDCTextFieldType.Outlined,
                    maxLength = 1024.toUInt(),
                    label = "Link ${index + 1}",
                    attrs = {
                        onInput { inputEvent ->
                            text = inputEvent.value
                        }
                    })
            }
        }
        MDCButton(attrs = {
            onClick {
                handleClick()
            }
        }) {
            if (isNew()) {
                NewDashboardActionButtonContent()
            } else {
                ExistingDashboardActionButtonContent()
            }
        }
    }
}

@Composable
private fun MDCButtonScope<*>.NewDashboardActionButtonContent() {
    Icon(attrs = {
        mdcIcon()
    }) { Text(MDCIcon.Cancel.type) }
    Label("Cancel")
}

@Composable
private fun MDCButtonScope<*>.ExistingDashboardActionButtonContent() {
    Icon(attrs = {
        mdcIcon()
    }) { Text(MDCIcon.Edit.type) }
    Label("Edit")
}
