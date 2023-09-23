/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities

interface UpdateDashboardInteractor {
    suspend fun updateDashboard(id: String, targets: List<String>, name: String, switchDelaySeconds: Int): Entities.WebPagesDashboard
    suspend fun delete(id: DashboardId)
}