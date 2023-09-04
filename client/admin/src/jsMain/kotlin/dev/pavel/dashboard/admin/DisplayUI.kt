package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import dev.pavel.dashboard.admin.displays.DisplayPM
import dev.petuska.kmdc.elevation.MDCElevation
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Composable
fun DisplayPM.Render() {
    @Suppress("UNUSED_VARIABLE") val pm = this
    MDCElevation(z = 3, attrs = {
        style {
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
            padding(8.px)
        }
    }) {
        Text("This is a display")
    }
}