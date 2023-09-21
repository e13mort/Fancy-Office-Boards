package dev.pavel.dashboard.resources

import dev.pavel.dashboard.entity.DashboardId
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/display")
class Display(@Suppress("unused") val parent: RestApi = RestApi()) {
    @Resource("{id}")
    class Id(@Suppress("unused") val parent: Display = Display(), val id: String) {
        @Resource("edit")
        class Update(val id: Id)
    }

}
@Serializable
data class UpdateDisplayContent(
    val name: String,
    val description: String,
    val dashboardId: DashboardId?
)
