/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.resources

import dev.pavel.dashboard.entity.DashboardId
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/dashboard")
class Dashboard(@Suppress("unused") val parent: RestApi = RestApi()) {
    @Resource("{id}")
    class Id(@Suppress("unused") val parent: Dashboard = Dashboard(), val id: DashboardId) {
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
