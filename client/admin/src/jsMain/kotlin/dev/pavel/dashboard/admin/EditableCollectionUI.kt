package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.admin.dashboards.EditableCollectionPM
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
import me.dmdev.premo.PresentationModel
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.px

const val COLUMNS = 2
const val MAX_COLUMNS = 12

@Composable
inline fun <reified T: PresentationModel>EditableCollectionPM<*, T>.Render(
    crossinline childRender: @Composable (T) -> Unit
) {
    val pm = this
    LaunchedEffect("Load") {
        load()
    }
    when(val state = dashboards().collectAsState().value) {
        is EditableCollectionPM<*, *>.ItemsState -> {
            ShowItems(state, childRender, pm)
        }
        EditableCollectionPM.LOADING -> ShowLoading()
    }
}
@Composable
fun ShowLoading() {
    MDCLinearProgress {

    }
}

@Composable
inline fun <reified T : PresentationModel> ShowItems(
    state: EditableCollectionPM<*, *>.ItemsState,
    crossinline childRender: @Composable (T) -> Unit,
    pm: EditableCollectionPM<*, T>
) {
    MDCLayoutGrid {
        val items: List<PresentationModel> = state.items
        val rows = items.windowed(COLUMNS, COLUMNS, true)
        rows.forEach { row ->
            Cells {
                row.forEach { item: PresentationModel ->
                    Cell(span = (MAX_COLUMNS / COLUMNS).toUInt()) {
                        childRender(item as T)
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
                MDCFab(
                    type = MDCFabType.Extended,
                    exited = state.addButtonEnabled().not(),
                    attrs = {
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