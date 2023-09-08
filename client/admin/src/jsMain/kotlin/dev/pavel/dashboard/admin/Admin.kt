package dev.pavel.dashboard.admin

import androidx.compose.runtime.remember
import dev.pavel.dashboard.admin.ui.Render
import dev.pavel.dashboard.fakes.MemoryDisplayRepository
import dev.pavel.dashboard.interactors.CreateDashboardInteractorImpl
import dev.pavel.dashboard.interactors.DisplaysInteractor
import dev.pavel.dashboard.interactors.UpdateDashboardInteractorImpl
import dev.pavel.dashboard.interactors.WebPagesDashboardInteractorImpl
import dev.pavel.dashboard.repository.HttpDashboardRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        val pm = remember {
            Admin.createPMDelegate(createDependencies()).let { delegate ->
                delegate.onCreate()
                delegate.onForeground()
                delegate.presentationModel
            }
        }
        pm.Render()
    }
}

// TODO: delegate to DI framework
fun createDependencies(): Admin.Dependencies {
    val dashboardRepository = HttpDashboardRepository(
        createHttpClient()
    )
    val displayRepository = MemoryDisplayRepository()
    val backgroundDispatcher = Dispatchers.Main
    return Admin.Dependencies(
        WebPagesDashboardInteractorImpl(dashboardRepository, backgroundDispatcher),
        UpdateDashboardInteractorImpl(dashboardRepository, backgroundDispatcher),
        CreateDashboardInteractorImpl(dashboardRepository, backgroundDispatcher),
        DisplaysInteractor(displayRepository, backgroundDispatcher)
    )
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