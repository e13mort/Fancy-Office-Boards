/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.resources

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DisplayId
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/display")
class Display(@Suppress("unused") val parent: RestApi = RestApi()) {
    @Resource("{id}")
    class Id(@Suppress("unused") val parent: Display = Display(), val id: DisplayId) {
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
