/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities
import kotlinx.serialization.Serializable

@Serializable
class DataWebDashboard(
    private val targets: List<String>,
    private val switchTimeoutSeconds: Int,
    private val name: String,
    private val id: DashboardId
) : Entities.WebPagesDashboard {
    override fun targets(): List<String> {
        return targets
    }

    override fun switchTimeoutSeconds(): Int {
        return switchTimeoutSeconds
    }

    override fun name(): String {
        return name
    }

    override fun id(): DashboardId {
        return id
    }

}