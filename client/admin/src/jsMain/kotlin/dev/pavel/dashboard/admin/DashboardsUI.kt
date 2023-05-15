package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.petuska.kmdc.elevation.MDCElevation
import dev.petuska.kmdc.fab.Label
import dev.petuska.kmdc.fab.MDCFab
import dev.petuska.kmdc.fab.MDCFabType
import dev.petuska.kmdc.layout.grid.Cell
import dev.petuska.kmdc.layout.grid.Cells
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.typography.MDCBody1
import dev.petuska.kmdcx.icons.MDCIcon
import dev.petuska.kmdcx.icons.MDCIconBase
import dev.petuska.kmdcx.icons.MDCIconType
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.px

private const val COLUMNS = 2
private const val MAX_COLUMNS = 12
@Composable
fun DashboardsPM.Render() {
    MDCLayoutGrid {
        val state = dashboards().collectAsState().value
        val rows = state.dashboards.windowed(COLUMNS, COLUMNS, true)
        rows.forEach { row ->
            Cells {
                row.forEach { item: DashboardsPM.Dashboard ->
                    Cell(span = (MAX_COLUMNS / COLUMNS).toUInt()) {
                        MDCElevation(z = 3, attrs = {
                            style {
                                alignItems(AlignItems.Center)
                                justifyContent(JustifyContent.Center)
                            }
                        }) {
                            MDCBody1(item.name)
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
                        addNewDashboard()
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