/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.dashboards

import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.CreateDashboardInteractor

internal class TestCreateDashboardInteractor : CreateDashboardInteractor {
    override suspend fun createDashboard(
        targets: List<String>, name: String, switchDelaySeconds: Int
    ): Entities.WebPagesDashboard {
        TODO("Not yet implemented")
    }
}