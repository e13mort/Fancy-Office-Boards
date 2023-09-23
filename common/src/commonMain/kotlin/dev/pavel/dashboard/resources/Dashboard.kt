package dev.pavel.dashboard.resources

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/dashboard")
class Dashboard(@Suppress("unused") val parent: RestApi = RestApi()) {
    @Resource("{id}")
    class Id(@Suppress("unused") val parent: Dashboard = Dashboard(), val id: String) {
        @Resource("edit")
        class Update(val id: Id)
    }

}
@Serializable
data class UpdateContent(
    val items: List<String>,
    val name: String,
    val switchTimeoutSeconds: Int
)
