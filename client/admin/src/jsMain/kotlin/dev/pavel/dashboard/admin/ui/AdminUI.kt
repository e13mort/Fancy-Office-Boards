/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.admin.AdminPM
import dev.pavel.dashboard.admin.dashboards.DashboardPM
import dev.pavel.dashboard.admin.dashboards.DashboardsPM
import dev.pavel.dashboard.admin.displays.DisplayPM
import dev.pavel.dashboard.admin.displays.DisplaysPM
import dev.pavel.dashboard.admin.ui.dasboard.Render
import dev.pavel.dashboard.admin.ui.display.Render
import dev.petuska.kmdc.tab.Content
import dev.petuska.kmdc.tab.Label
import dev.petuska.kmdc.tab.Tab
import dev.petuska.kmdc.tab.bar.MDCTabBar
import dev.petuska.kmdc.tab.indicator.Indicator
import dev.petuska.kmdc.tab.indicator.Underline
import dev.petuska.kmdc.tab.onInteracted
import dev.petuska.kmdc.tab.scroller.Scroller

@Composable
fun AdminPM.Render() {
    RenderMaster()
    when (val detailsPM = navigation.detailFlow.collectAsState().value) {
        is DisplaysPM -> detailsPM.Render { displayPM: DisplayPM ->
            displayPM.Render()
        }
        is DashboardsPM -> detailsPM.Render { dashboardPM: DashboardPM ->
            dashboardPM.Render()
        }
    }
}

@Composable
private fun AdminPM.RenderMaster() {
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