package dev.pavel.dashboard

import dev.pavel.dashboard.fakes.MemoryDashboardsRepository
import dev.pavel.dashboard.resources.Dashboard
import dev.pavel.dashboard.resources.UpdateContent
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.routing

fun Application.installRestApi(authProviderName: String) {
    val dashboardsRepository = MemoryDashboardsRepository.create(0) //todo: use persisted storage
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
        authenticate(authProviderName) {
            put<Dashboard.Id.Update> { updateInfo ->
                val id = updateInfo.id.id
                val updateContent = call.receive<UpdateContent>()
                dashboardsRepository.updateDashboard(id, updateContent.items, updateContent.name)
                call.respond(HttpStatusCode.OK)
            }
            post<Dashboard> {
                val updateContent = call.receive<UpdateContent>()
                val newDashboardId =
                    dashboardsRepository.createDashboard(updateContent.items, updateContent.name)
                call.respond(HttpStatusCode.Created, newDashboardId)
            }
        }
    }
}