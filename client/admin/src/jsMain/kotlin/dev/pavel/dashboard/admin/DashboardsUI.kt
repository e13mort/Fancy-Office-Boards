package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.admin.dashboards.DashboardPM
import dev.pavel.dashboard.admin.dashboards.DashboardsPM
import dev.petuska.kmdc.fab.Label
import dev.petuska.kmdc.fab.MDCFab
import dev.petuska.kmdc.fab.MDCFabType
import dev.petuska.kmdc.layout.grid.Cell
import dev.petuska.kmdc.layout.grid.Cells
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.linear.progress.MDCLinearProgress
import dev.petuska.kmdcx.icons.MDCIcon
import dev.petuska.kmdcx.icons.MDCIconBase
import dev.petuska.kmdcx.icons.MDCIconType
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.px

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
                row.forEach { item: DashboardPM ->
                    Cell(span = (MAX_COLUMNS / COLUMNS).toUInt()) {
                        item.Render()
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
                MDCFab(type = MDCFabType.Extended, exited = state.addButtonEnabled().not(), attrs = {
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