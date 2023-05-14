package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
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

@Composable
fun DashboardsPM.Render() {
    MDCLayoutGrid {
        Cells {
            repeat(2) { i ->
                Cell(span = 6.toUInt()) {
                    MDCElevation(z = 3, attrs = {
                        style {
                            alignItems(AlignItems.Center)
                            justifyContent(JustifyContent.Center)
                        }
                    }) {
                        MDCBody1("Dashboard $i")
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