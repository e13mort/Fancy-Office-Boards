package dev.pavel.dashboard.admin.ui.display

import androidx.compose.runtime.Composable
import dev.pavel.dashboard.admin.displays.DisplayPM
import dev.pavel.dashboard.admin.ui.RenderAsTitle
import dev.pavel.dashboard.admin.ui.RenderCollection

@Composable
fun DisplayPM.Render() {
    RenderCollection { isActive ->
        name.RenderAsTitle(
            isActive = isActive,
            label = "Name"
        )
    }
}