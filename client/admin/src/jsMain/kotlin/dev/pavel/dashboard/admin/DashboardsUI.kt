package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.petuska.kmdc.button.Icon
import dev.petuska.kmdc.button.Label
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.elevation.MDCElevation
import dev.petuska.kmdc.fab.Label
import dev.petuska.kmdc.fab.MDCFab
import dev.petuska.kmdc.fab.MDCFabType
import dev.petuska.kmdc.layout.grid.Cell
import dev.petuska.kmdc.layout.grid.Cells
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.linear.progress.MDCLinearProgress
import dev.petuska.kmdc.textfield.MDCTextField
import dev.petuska.kmdc.textfield.MDCTextFieldType
import dev.petuska.kmdc.typography.MDCBody1
import dev.petuska.kmdcx.icons.MDCIcon
import dev.petuska.kmdcx.icons.MDCIconBase
import dev.petuska.kmdcx.icons.MDCIconType
import dev.petuska.kmdcx.icons.mdcIcon
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

private const val COLUMNS = 2
private const val MAX_COLUMNS = 12
@Composable
fun DashboardsPM.Render() {
    val pm = this
    LaunchedEffect("Load") {
        load()
    }
    when(val state = dashboards().collectAsState().value) {
        is DashboardsPM.ItemsState -> ShowItems(state, pm)
        DashboardsPM.LOADING -> ShowLoading()
    }

}

@Composable
fun ShowLoading() {
    MDCLinearProgress {

    }
}

@Composable
private fun ShowItems(
    state: DashboardsPM.ItemsState,
    pm: DashboardsPM
) {

    MDCLayoutGrid {
        val rows = state.dashboards.windowed(COLUMNS, COLUMNS, true)
        rows.forEach { row ->
            Cells {
                row.forEach { item: DashboardsPM.Dashboard ->
                    Cell(span = (MAX_COLUMNS / COLUMNS).toUInt()) {
                        MDCElevation(z = 3, attrs = {
                            style {
                                alignItems(AlignItems.Center)
                                justifyContent(JustifyContent.Center)
                                padding(8.px)
                            }
                        }) {
                            MDCBody1(item.name)
                            item.links.forEachIndexed { index, link ->
                                Div(attrs = {
                                    style {
                                        paddingBottom(4.px)
                                    }
                                }) {
                                    var text by remember { mutableStateOf(link) }
                                    MDCTextField(text, type = MDCTextFieldType.Outlined, maxLength = 1024.toUInt(), label = "Link ${index + 1}" ,attrs = {
                                        onInput { inputEvent ->
                                            text = inputEvent.value
                                        }
                                    })
                                }
                            }
                            MDCButton {
                                Icon(attrs = { mdcIcon() }) { Text(MDCIcon.Edit.type) }
                                Label("Edit")
                            }
                        }
                    }
                }
            }

        }
        Cells {
            Cell(span = 12.toUInt(), attrs = {
                style {
                    property("margin-left", "auto")
                    marginRight(0.px)
                }
            }) {
                MDCFab(type = MDCFabType.Extended, attrs = {
                    attr("aria-label", "Add")
                    onClick {
                        pm.addNewDashboard()
                    }
                }) {
                    Label("Add")
                    MDCIcon(
                        base = MDCIconBase.Span,
                        type = MDCIconType.Filled,
                        icon = MDCIcon.Add,
                    )
                }

            }
        }
    }
}