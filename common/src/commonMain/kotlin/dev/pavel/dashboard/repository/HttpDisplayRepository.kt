/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.repository

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DataDisplay
import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.resources.Display
import dev.pavel.dashboard.resources.UpdateDisplayContent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.href
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class HttpDisplayRepository(
    private val httpClient: HttpClient,
) : DisplayRepository {
    override suspend fun updateDisplay(
        displayId: DisplayId,
        name: String,
        description: String,
        dashboardId: DashboardId?
    ) {
        val updateDisplayContent = UpdateDisplayContent(name, description, dashboardId)
        val updateResource = Display.Id.Update(id = Display.Id(id = displayId))
        httpClient.put(httpClient.href(updateResource)) {
            contentType(ContentType.Application.Json)
            setBody(updateDisplayContent)
        }
    }

    override suspend fun createDisplay(
        name: String,
        description: String,
        dashboardId: DashboardId?
    ): DisplayId {
        val updateDisplayContent = UpdateDisplayContent(name, description, dashboardId)
        val urlString = httpClient.href(Display())
        val httpResponse = httpClient.post(urlString) {
            contentType(ContentType.Application.Json)
            setBody(updateDisplayContent)
        }
        if (httpResponse.status == HttpStatusCode.Created) {
            return httpResponse.body()
        }
        throw IllegalStateException("Server responded with code: ${httpResponse.status}")
    }

    override suspend fun allDisplays(): List<DataDisplay> {
        return httpClient.get(httpClient.href(Display())).body()
    }

    override suspend fun delete(id: DisplayId) {
        val delete = httpClient.delete(httpClient.href(Display.Id(id = id)))
        val status = delete.status
        if (status != HttpStatusCode.OK) {
            throw IllegalStateException("Server responded with code: $status")
        }
    }
}