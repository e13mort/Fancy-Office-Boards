package dev.pavel.dashboard.admin.ui.display

import androidx.compose.runtime.Composable
import dev.pavel.dashboard.admin.displays.DisplayPM
import dev.pavel.dashboard.admin.properties.ListProperty
import dev.pavel.dashboard.admin.ui.RenderAsDescription
import dev.pavel.dashboard.admin.ui.RenderAsTitle
import dev.pavel.dashboard.admin.ui.RenderCollection
import dev.petuska.kmdc.core.MDCEvent
import dev.petuska.kmdc.select.MDCSelect
import dev.petuska.kmdc.select.MDCSelectChangeEventDetail
import dev.petuska.kmdc.select.MDCSelectType
import dev.petuska.kmdc.select.anchor.Anchor
import dev.petuska.kmdc.select.menu.Menu
import dev.petuska.kmdc.select.menu.SelectItem
import dev.petuska.kmdc.select.onChange
import org.jetbrains.compose.web.dom.Div

@Composable
fun DisplayPM.Render() {
    RenderCollection { isActive ->
        name.RenderAsTitle(
            isActive = isActive,
            label = "Name"
        )
        description.RenderAsDescription(
            isActive = isActive
        )
        dashboard.Render(
            isActive = isActive
        )
    }
}

@Composable
fun ListProperty<String>.Render(isActive: Boolean) {
    Div {
        MDCSelect(
            type = MDCSelectType.Outlined,
            attrs = {
                onChange { it: MDCEvent<MDCSelectChangeEventDetail> ->
                    value = listItems[it.detail.index]
                }
            },
            disabled = !isActive
        ) {
            Anchor("Dashboard")
            Menu {
                listItems.forEach{
                    SelectItem(it.name, selected = initValue == it.data)
                }
            }
        }
    }
}