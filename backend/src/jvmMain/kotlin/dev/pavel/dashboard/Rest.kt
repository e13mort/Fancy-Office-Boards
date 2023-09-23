/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard

import dev.pavel.dashboard.fakes.MemoryDashboardsRepository
import dev.pavel.dashboard.fakes.MemoryDisplayRepository
import dev.pavel.dashboard.resources.Dashboard
import dev.pavel.dashboard.resources.Display
import dev.pavel.dashboard.resources.UpdateContent
import dev.pavel.dashboard.resources.UpdateDisplayContent
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.routing

fun Application.installRestApi(authProviderName: String) {
    val dashboardsRepository = MemoryDashboardsRepository.create(0) //todo: use persisted storage
    val displayRepository = MemoryDisplayRepository()
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
        get<Display.Id> { display ->
            call.respond(displayRepository.allDisplays()
                .filter {
                    it.id() == display.id
                })
        }
        get<Display> {
            call.respond(displayRepository.allDisplays())
        }
        authenticate(authProviderName) {
            put<Dashboard.Id.Update> { updateInfo ->
                val id = updateInfo.id.id
                val updateContent = call.receive<UpdateContent>()
                dashboardsRepository.updateDashboard(
                    id,
                    updateContent.items,
                    updateContent.name,
                    updateContent.switchTimeoutSeconds
                )
                call.respond(HttpStatusCode.OK)
            }
            post<Dashboard> {
                val updateContent = call.receive<UpdateContent>()
                val newDashboardId =
                    dashboardsRepository.createDashboard(
                        updateContent.items,
                        updateContent.name,
                        updateContent.switchTimeoutSeconds
                    )
                call.respond(HttpStatusCode.Created, newDashboardId)
            }
            put<Display.Id.Update> { updateInfo ->
                val id = updateInfo.id.id
                val updateContent = call.receive<UpdateDisplayContent>()
                displayRepository.updateDisplay(
                    id,
                    updateContent.name,
                    updateContent.description,
                    updateContent.dashboardId
                )
                call.respond(HttpStatusCode.OK)
            }
            post<Display> {
                val displayContent = call.receive<UpdateDisplayContent>()
                val newDisplayId = displayRepository.createDisplay(
                    displayContent.name,
                    displayContent.description,
                    displayContent.dashboardId
                )
                call.respond(HttpStatusCode.Created, newDisplayId)
            }
            delete<Display.Id> { display ->
                displayRepository.delete(display.id)
                call.respond(HttpStatusCode.OK)
            }
            delete<Dashboard.Id> { dashboard ->
                dashboardsRepository.delete(dashboard.id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}