package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.tab.Content
import dev.petuska.kmdc.tab.Label
import dev.petuska.kmdc.tab.Tab
import dev.petuska.kmdc.tab.bar.MDCTabBar
import dev.petuska.kmdc.tab.indicator.Indicator
import dev.petuska.kmdc.tab.indicator.Underline
import dev.petuska.kmdc.tab.onInteracted
import dev.petuska.kmdc.tab.scroller.Scroller

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