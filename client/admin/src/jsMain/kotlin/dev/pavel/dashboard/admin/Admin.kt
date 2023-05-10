package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.tab.Content
import dev.petuska.kmdc.tab.Label
import dev.petuska.kmdc.tab.Tab
import dev.petuska.kmdc.tab.bar.MDCTabBar
import dev.petuska.kmdc.tab.indicator.Indicator
import dev.petuska.kmdc.tab.indicator.Underline
import dev.petuska.kmdc.tab.scroller.Scroller
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        RenderUI()
    }
}

@Composable
private fun RenderUI() {
    MDCLayoutGrid {
        MDCTabBar {
            Scroller {
                Tab {
                    Content {
                        Label("Displays")
                    }
                    Indicator(active = true) {
                        Underline()
                    }
                }
                Tab {
                    Content {
                        Label("Dashboards")
                    }
                    Indicator(active = false) {
                        Underline()
                    }
                }
            }
        }
    }
}