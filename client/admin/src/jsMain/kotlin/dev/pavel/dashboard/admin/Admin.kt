package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.tab.Content
import dev.petuska.kmdc.tab.Label
import dev.petuska.kmdc.tab.Tab
import dev.petuska.kmdc.tab.bar.MDCTabBar
import dev.petuska.kmdc.tab.indicator.Indicator
import dev.petuska.kmdc.tab.indicator.Underline
import dev.petuska.kmdc.tab.onInteracted
import dev.petuska.kmdc.tab.scroller.Scroller
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        val pm = remember {
            Admin.createPMDelegate().let { delegate ->
                delegate.onCreate()
                delegate.onForeground()
                delegate.presentationModel
            }
        }
        RenderUI(pm)
    }
}

@Composable
private fun RenderUI(pm: AdminPM) {
    MDCLayoutGrid {
        pm.RenderMaster()
        when (val detailsPM = pm.navigation.detailFlow.collectAsState().value) {
            is DisplaysPM -> detailsPM.Render()
            is DashboardsPM -> detailsPM.Render()
        }
    }
}

@Composable
fun DisplaysPM.Render() {
    Text("Displays")
}

@Composable
fun DashboardsPM.Render() {
    Text("Dashboards")
}

@Composable
fun AdminPM.RenderMaster() {
    MDCTabBar {
        Scroller {
            activeTabs().forEach { tab ->
                Tab(attrs = {
                    onInteracted { tab.activate() }
                }) {
                    Content {
                        Label(tab.title)
                    }
                    Indicator(active = true) {
                        Underline()
                    }
                }
            }
        }
    }
}