package dev.pavel.dashboard.repository

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.entity.WebPagesDashboardRepository
import dev.pavel.dashboard.fakes.DataWebDashboard
import dev.pavel.dashboard.resources.Dashboard
import dev.pavel.dashboard.resources.UpdateContent
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
import kotlinx.coroutines.flow.Flow

class HttpDashboardRepository(
    private val httpClient: HttpClient,
) : WebPagesDashboardRepository {

    override suspend fun updateDashboard(id: DashboardId, targets: List<String>, name: String) {
        val updateContent = UpdateContent(
            targets, name
        )
        val updateResource = Dashboard.Id.Update(id = Dashboard.Id(id = id))
        httpClient.put(httpClient.href(updateResource)) {
            contentType(ContentType.Application.Json)
            setBody(updateContent)
        }
    }

    override suspend fun createDashboard(targets: List<String>, name: String): DashboardId {
        val updateContent = UpdateContent(
            targets, name
        )
        val urlString = httpClient.href(Dashboard())
        val httpResponse = httpClient.post(urlString) {
            contentType(ContentType.Application.Json)
            setBody(updateContent)
        }
        if (httpResponse.status == HttpStatusCode.Created) {
            return httpResponse.body()
        }
        throw IllegalStateException("Server responded with code: ${httpResponse.status}")
    }

    override fun observeDashboardById(id: DashboardId): Flow<Entities.WebPagesDashboard> {
        TODO("Not yet implemented")
    }

    override suspend fun findDashboardById(id: DashboardId): DataWebDashboard? {
        return httpClient.get(httpClient.href(Dashboard.Id(id = id))).body()
    }

    override suspend fun allDashboards(): List<DataWebDashboard> {
        return httpClient.get(httpClient.href(Dashboard())).body()
    }

    override suspend fun delete(id: DashboardId) {
        val status = httpClient.delete(httpClient.href(Dashboard.Id(id = id))).status
        if (status != HttpStatusCode.OK) {
            throw IllegalStateException("Server responded with code: $status")
        }
    }
}