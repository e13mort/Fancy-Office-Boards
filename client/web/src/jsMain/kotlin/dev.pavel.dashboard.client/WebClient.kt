package dev.pavel.dashboard.client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Presenter
import dev.pavel.dashboard.entity.StubPresenter
import dev.pavel.dashboard.repository.HttpDashboardRepository
import dev.pavel.dashboard.repository.HttpDisplayRepository
import dev.petuska.kmdc.card.MDCCard
import dev.petuska.kmdc.card.MDCCardMediaType
import dev.petuska.kmdc.card.MDCCardType
import dev.petuska.kmdc.card.Media
import dev.petuska.kmdc.card.PrimaryAction
import dev.petuska.kmdc.layout.grid.Cell
import dev.petuska.kmdc.layout.grid.Cells
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.typography.MDCH4
import dev.petuska.kmdc.typography.MDCSubtitle1
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
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
    val presenter = StubPresenter(
        displayRepository, dashboardRepository,
        Dispatchers.Main
    )
    renderComposable(rootElementId = "root") {
        LaunchedEffect(Unit) {
            presenter.start()
        }
        when(val uiStateState = presenter.observeUIStates().collectAsState().value) {
            is Presenter.UIState.Error -> ShowError(uiStateState)
            Presenter.UIState.Loading -> ShowLoading()
            is Presenter.UIState.WebPage -> ShowPage(uiStateState)
            is Presenter.UIState.Displays -> ShowDisplays(uiStateState) { id ->
                presenter.onDisplaySelected(id)
            }
        }
    }
}

@Composable
fun ShowDisplays(value: Presenter.UIState.Displays, click: (DashboardId?) -> Unit) {
    MDCLayoutGrid {
        val items = value.displays
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
                                    onClick {
                                        click(item.dashboardId())
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
fun ShowError(uiState: Presenter.UIState.Error) {
    H1 {
        Text("Error: ${uiState.type}")
    }
}

@Composable
fun ShowPage(value: Presenter.UIState.WebPage) {
    Iframe(attrs = {
        attr("src", value.path)
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
