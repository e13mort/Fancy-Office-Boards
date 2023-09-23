/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.UpdateDashboardInteractor

internal class TestUpdateDashboardInteractor : UpdateDashboardInteractor {
    override suspend fun updateDashboard(
        id: String, targets: List<String>, name: String, switchDelaySeconds: Int
    ): Entities.WebPagesDashboard {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: DashboardId) {
        TODO("Not yet implemented")
    }

}