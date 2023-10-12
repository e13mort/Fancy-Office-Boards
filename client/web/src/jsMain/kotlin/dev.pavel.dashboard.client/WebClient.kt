/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.WebClientApp
import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.pm.DisplayListPM
import dev.pavel.dashboard.pm.WebPagesDashboardPM
import dev.pavel.dashboard.repository.HttpDashboardRepository
import dev.pavel.dashboard.repository.HttpDisplayRepository
import dev.petuska.kmdc.card.MDCCard
import dev.petuska.kmdc.card.MDCCardMediaType
import dev.petuska.kmdc.card.MDCCardType
import dev.petuska.kmdc.card.Media
import dev.petuska.kmdc.card.PrimaryAction
import dev.petuska.kmdc.fab.MDCFab
import dev.petuska.kmdc.fab.MDCFabType
import dev.petuska.kmdc.layout.grid.Cell
import dev.petuska.kmdc.layout.grid.Cells
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.typography.MDCH4
import dev.petuska.kmdc.typography.MDCSubtitle1
import dev.petuska.kmdcx.icons.MDCIcon
import dev.petuska.kmdcx.icons.MDCIconBase
import dev.petuska.kmdcx.icons.MDCIconType
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import kotlinx.browser.window
import me.dmdev.premo.PresentationModel
import me.dmdev.premo.navigation.back
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingRight
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Iframe
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    val httpClient = createHttpClient()
    val displayRepository = HttpDisplayRepository(httpClient)
    val dashboardRepository = HttpDashboardRepository(httpClient)
    val appPM = WebClientApp.createPMDelegate(
        WebClientApp.Dependencies(displayRepository, dashboardRepository)
    ).presentationModel
    renderComposable(rootElementId = "root") {
        when (val currentPM = appPM.navigation.currentTopFlow.collectAsState().value) {
            is PresentationModel -> currentPM.Render()
            else -> ShowLoading()
        }
    }
}

@Composable
fun PresentationModel.Render() {
    when (this) {
        is DisplayListPM -> Render()
        is WebPagesDashboardPM -> Render()
    }
}

@Composable
fun DisplayListPM.Render() {
    LaunchedEffect(this) {
        load()
    }
    val activeDisplays = displays.collectAsState().value
    if (activeDisplays.isEmpty()) {
        ShowLoading()
    } else {
        ShowDisplays(activeDisplays) {
            onDashboardSelected(it)
        }
    }
}

@Composable
fun WebPagesDashboardPM.Render() {
    LaunchedEffect(this) {
        start()
    }
    when (val uiState = currentPage.collectAsState().value) {
        WebPagesDashboardPM.WebPagesUIState.Error -> ShowError()
        WebPagesDashboardPM.WebPagesUIState.Loading -> ShowLoading()
        is WebPagesDashboardPM.WebPagesUIState.WebPage -> ShowPage(uiState.link) {
            back()
        }
    }
}

@Composable
fun ShowDisplays(items: List<Entities.Display>, click: (DashboardId) -> Unit) {
    MDCLayoutGrid {
        val rows = items.windowed(2, 2, true)
        rows.forEach { row ->
            Cells {
                row.forEach { item ->
                    Cell(span = (12 / 2).toUInt()) {
                        MDCCard(
                            type = MDCCardType.Elevated
                        ) {
                            PrimaryAction(
                                attrs = {
                                    item.dashboardId()?.let { id ->
                                        onClick {
                                            click(id)
                                        }
                                    }
                                }
                            ) {
                                Media(
                                    type = MDCCardMediaType.Free,
                                ) {
                                    Div(
                                        attrs = {
                                            style {
                                                paddingLeft(8.px)
                                                paddingRight(8.px)
                                            }
                                        }

                                    ) {
                                        MDCH4(item.name())
                                        MDCSubtitle1(item.description())
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

}

@Composable
fun ShowError() {
    H1 {
        Text("Error")
    }
}

@Composable
fun ShowPage(value: String, click: () -> Unit) {
    MDCFab(type = MDCFabType.Mini, attrs = {
        attr("aria-label", "Displays")
        style {
            position(Position.Absolute)
            property("z-index", 2)
        }
        onClick {
            click()
        }
    }) {
        MDCIcon(
            base = MDCIconBase.Span,
            type = MDCIconType.Filled,
            icon = MDCIcon.List,
        )
    }
    Iframe(attrs = {
        attr("src", value)
        style {
            width(100.percent)
            height(100.percent)
            position(Position.Fixed)
        }
    })
}

@Composable
private fun ShowLoading() {
    H1 {
        Text("Loading...")
    }
}

private fun createHttpClient(): HttpClient {
    val runtimeHost = window.location.hostname
    val runtimePort = if (window.location.port.isNotEmpty())
        window.location.port.toInt() else 80
    val httpClient = HttpClient {
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            host = runtimeHost
            port = runtimePort
        }
    }
    return httpClient
}
