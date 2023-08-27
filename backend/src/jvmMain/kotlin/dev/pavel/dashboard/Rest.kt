package dev.pavel.dashboard

import dev.pavel.dashboard.fakes.MemoryDashboardsRepository
import dev.pavel.dashboard.resources.Dashboard
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.routing

fun Application.installRestApi() {
    // temp
    val dashboardsRepository = MemoryDashboardsRepository.create()
    routing {
        get<Dashboard.Id> { dashboard ->
            val webPagesDashboard = dashboardsRepository.findDashboardById(dashboard.id)
            if (webPagesDashboard != null) {
                call.respond(webPagesDashboard)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        get<Dashboard> {
            call.respond(dashboardsRepository.allDashboards())
        }
    }
}