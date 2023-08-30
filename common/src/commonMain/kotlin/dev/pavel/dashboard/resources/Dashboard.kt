package dev.pavel.dashboard.resources

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Suppress("unused")
@Resource("/dashboard")
class Dashboard(val parent: RestApi = RestApi()) {
    @Resource("{id}")
    class Id(val parent: Dashboard = Dashboard(), val id: String) {
        @Resource("edit")
        class Update(val id: Id)
    }

}
@Serializable
data class UpdateContent(
    val items: List<String>,
    val name: String
)
